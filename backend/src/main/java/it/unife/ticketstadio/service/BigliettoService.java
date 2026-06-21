package it.unife.ticketstadio.service;
import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.*;
import it.unife.ticketstadio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional
public class BigliettoService {
    private final BigliettoRepository bigliettoRepo;
    private final PartitaRepository partitaRepo;
    private final PostoRepository postoRepo;
    private final PromozioneRepository promoRepo;
    private final PagamentoRepository pagamentoRepo;
    private final UtenteRepository utenteRepo;
    public BigliettoResponse acquista(AcquistoBigliettoRequest req,String email){
        Utente utente=utenteRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Utente"));
        Partita partita=partitaRepo.findById(req.getPartitaId()).orElseThrow(()->new ResourceNotFoundException("Partita"));
        Posto posto=postoRepo.findById(req.getPostoId()).orElseThrow(()->new ResourceNotFoundException("Posto"));
        if(!partita.isAcquistabile()) throw new IllegalStateException("Partita non disponibile per l'acquisto");
        if(bigliettoRepo.existsByPartitaAndPosto(partita,posto)) throw new PostoOccupatoException("Posto già occupato per questa partita");
        BigDecimal prezzo=posto.getSettore().getPrezzoBase();
        if(req.getCodicePromo()!=null&&!req.getCodicePromo().isBlank()){
            Promozione promo=promoRepo.findByCodice(req.getCodicePromo()).orElseThrow(()->new IllegalArgumentException("Codice promo non valido"));
            if(!promo.isValida(partita)) throw new IllegalArgumentException("Promozione non applicabile");
            prezzo=prezzo.multiply(BigDecimal.ONE.subtract(promo.getScontoPercentuale().divide(BigDecimal.valueOf(100)))).setScale(2,RoundingMode.HALF_UP);
        }
        Biglietto b=bigliettoRepo.save(Biglietto.builder().partita(partita).posto(posto).utente(utente).prezzoPagato(prezzo).dataAcquisto(LocalDateTime.now()).build());
        pagamentoRepo.save(Pagamento.builder().biglietto(b).metodo(Pagamento.Metodo.valueOf(req.getMetodoPagamento())).importo(prezzo).data(LocalDateTime.now()).stato(Pagamento.Stato.COMPLETATO).build());
        return BigliettoResponse.from(b);
    }
    @Transactional(readOnly=true)
    public List<BigliettoResponse> getMiei(String email){return bigliettoRepo.findByUtenteEmail(email).stream().map(BigliettoResponse::from).toList();}
    @Transactional(readOnly=true)
    public BigliettoResponse getById(Long id,String email){
        Biglietto b=bigliettoRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Biglietto"));
        if(!b.getUtente().getEmail().equals(email)) throw new SecurityException("Non autorizzato");
        return BigliettoResponse.from(b);
    }
}
