package it.unife.ticketstadio.service;
import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.ResourceNotFoundException;
import it.unife.ticketstadio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional
public class PartitaService {
    private final PartitaRepository partitaRepo;
    private final SquadraRepository squadraRepo;
    private final StadioRepository stadioRepo;
    private final SettoreRepository settoreRepo;
    private final BigliettoRepository bigliettoRepo;
    @Transactional(readOnly=true)
    public List<PartitaResponse> getAll(){return partitaRepo.findAll().stream().map(p->PartitaResponse.from(p,getPrezzoMin(p))).toList();}
    @Transactional(readOnly=true)
    public PartitaResponse getById(Long id){Partita p=partitaRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Partita"));return PartitaResponse.from(p,getPrezzoMin(p));}
    public PartitaResponse crea(PartitaRequest req){
        if(req.getSquadraCasaId().equals(req.getSquadraOspiteId())) throw new IllegalArgumentException("Le due squadre non possono essere uguali");
        Squadra c=squadraRepo.findById(req.getSquadraCasaId()).orElseThrow(()->new ResourceNotFoundException("Squadra casa"));
        Squadra o=squadraRepo.findById(req.getSquadraOspiteId()).orElseThrow(()->new ResourceNotFoundException("Squadra ospite"));
        Stadio st=c.getHomeStadium()!=null?c.getHomeStadium():stadioRepo.findAll().stream().findFirst().orElseThrow(()->new ResourceNotFoundException("Stadio"));
        Partita p=partitaRepo.save(Partita.builder().squadraCasa(c).squadraOspite(o).dataOra(req.getDataOra()).stadio(st).stato(Partita.Stato.valueOf(req.getStato())).build());
        return PartitaResponse.from(p,getPrezzoMin(p));
    }
    public PartitaResponse aggiorna(Long id,PartitaRequest req){
        Partita p=partitaRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Partita"));
        if(req.getSquadraCasaId().equals(req.getSquadraOspiteId())) throw new IllegalArgumentException("Le due squadre non possono essere uguali");
        Squadra newCasa=squadraRepo.findById(req.getSquadraCasaId()).orElseThrow(()->new ResourceNotFoundException("Squadra"));
        p.setSquadraCasa(newCasa);
        p.setSquadraOspite(squadraRepo.findById(req.getSquadraOspiteId()).orElseThrow(()->new ResourceNotFoundException("Squadra")));
        if(newCasa.getHomeStadium()!=null) p.setStadio(newCasa.getHomeStadium());
        p.setDataOra(req.getDataOra());p.setStato(Partita.Stato.valueOf(req.getStato()));
        return PartitaResponse.from(partitaRepo.save(p),getPrezzoMin(p));
    }
    public void annulla(Long id){
        Partita p=partitaRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Partita"));
        p.setStato(Partita.Stato.ANNULLATA);partitaRepo.save(p);
        bigliettoRepo.findAllByPartitaId(id).forEach(b->{b.setStato(Biglietto.Stato.ANNULLATO);bigliettoRepo.save(b);});
    }
    private Double getPrezzoMin(Partita p){
        return settoreRepo.findAll().stream().filter(s->s.getStadio().getId().equals(p.getStadio().getId())).mapToDouble(s->s.getPrezzoBase().doubleValue()).min().orElse(0.0);
    }
}
