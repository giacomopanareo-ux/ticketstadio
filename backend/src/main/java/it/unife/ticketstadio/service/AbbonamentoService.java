package it.unife.ticketstadio.service;
import it.unife.ticketstadio.dto.AbbonamentoRequest;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.ResourceNotFoundException;
import it.unife.ticketstadio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional
public class AbbonamentoService {
    private final AbbonamentoRepository abbRepo;
    private final SettoreRepository settoreRepo;
    private final UtenteRepository utenteRepo;
    public Abbonamento sottoscrivi(AbbonamentoRequest req,String email){
        Utente u=utenteRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Utente"));
        Settore s=settoreRepo.findById(req.getSettoreId()).orElseThrow(()->new ResourceNotFoundException("Settore"));
        if(abbRepo.existsByUtenteAndSettoreAndStagione(u,s,req.getStagione())) throw new IllegalStateException("Abbonamento già attivo per questa stagione e settore");
        return abbRepo.save(Abbonamento.builder().utente(u).settore(s).stagione(req.getStagione()).dataInizio(req.getDataInizio()).dataFine(req.getDataFine()).prezzo(s.getPrezzoBase().multiply(BigDecimal.valueOf(20))).build());
    }
    public Abbonamento rinnova(Long id,String email){
        Abbonamento v=abbRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Abbonamento"));
        if(!v.getUtente().getEmail().equals(email)) throw new SecurityException("Non autorizzato");
        String[] p=v.getStagione().split("/");int af=Integer.parseInt(p[1]);String ns=af+"/"+(af+1);
        if(abbRepo.existsByUtenteAndSettoreAndStagione(v.getUtente(),v.getSettore(),ns)) throw new IllegalStateException("Rinnovo già effettuato");
        return abbRepo.save(Abbonamento.builder().utente(v.getUtente()).settore(v.getSettore()).stagione(ns).dataInizio(v.getDataFine().plusDays(1)).dataFine(v.getDataFine().plusYears(1)).prezzo(v.getPrezzo()).build());
    }
    @Transactional(readOnly=true)
    public List<Abbonamento> getMiei(String email){return abbRepo.findByUtenteEmail(email);}
}
