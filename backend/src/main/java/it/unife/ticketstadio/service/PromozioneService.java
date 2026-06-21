package it.unife.ticketstadio.service;
import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.ResourceNotFoundException;
import it.unife.ticketstadio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional
public class PromozioneService {
    private final PromozioneRepository promoRepo;
    private final PartitaRepository partitaRepo;
    @Transactional(readOnly=true) public List<Promozione> getAll(){return promoRepo.findAll();}
    public Promozione crea(PromozioneRequest req){
        if(promoRepo.findByCodice(req.getCodice()).isPresent()) throw new IllegalArgumentException("Codice già esistente");
        if(req.getDataFine().isBefore(req.getDataInizio())) throw new IllegalArgumentException("Data fine precedente a data inizio");
        Partita partita=req.getPartitaId()!=null?partitaRepo.findById(req.getPartitaId()).orElseThrow(()->new ResourceNotFoundException("Partita")):null;
        return promoRepo.save(Promozione.builder().codice(req.getCodice().toUpperCase()).descrizione(req.getDescrizione()).scontoPercentuale(req.getScontoPercentuale()).dataInizio(req.getDataInizio()).dataFine(req.getDataFine()).partita(partita).build());
    }
    public void disattiva(Long id){
        Promozione p=promoRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Promozione"));
        p.setDataFine(LocalDate.now().minusDays(1));promoRepo.save(p);
    }
    @Transactional(readOnly=true)
    public Promozione valida(ValidaPromoRequest req){
        Promozione p=promoRepo.findByCodice(req.getCodice().toUpperCase()).orElseThrow(()->new IllegalArgumentException("Codice promo non valido"));
        Partita pt=partitaRepo.findById(req.getPartitaId()).orElseThrow(()->new ResourceNotFoundException("Partita"));
        if(!p.isValida(pt)) throw new IllegalArgumentException("Promozione non applicabile a questa partita");
        return p;
    }
}
