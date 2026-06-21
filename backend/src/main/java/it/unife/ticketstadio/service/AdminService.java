package it.unife.ticketstadio.service;
import it.unife.ticketstadio.dto.StatisticheResponse;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.ResourceNotFoundException;
import it.unife.ticketstadio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor
public class AdminService {
    private final BigliettoRepository bigliettoRepo;
    private final AbbonamentoRepository abbRepo;
    private final PartitaRepository partitaRepo;
    @Transactional(readOnly=true)
    public StatisticheResponse getStatisticheVendite(){
        StatisticheResponse r=new StatisticheResponse();
        r.setTotaleBiglietti(bigliettoRepo.countValidi());
        r.setIncassoTotale(bigliettoRepo.sumIncasso());
        r.setAbbonamenti(abbRepo.count());
        r.setPartiteProgrammate(partitaRepo.findByStatoOrderByDataOraAsc(Partita.Stato.PROGRAMMATA).size());
        r.setPerPartita(partitaRepo.findAll().stream().map(p->{
            var d=new StatisticheResponse.VenditaPartitaDto();
            d.setPartitaId(p.getId());d.setSquadraCasa(p.getSquadraCasa().getNome());
            d.setSquadraOspite(p.getSquadraOspite().getNome());d.setDataOra(p.getDataOra().toString());d.setStato(p.getStato().name());
            var bl=bigliettoRepo.findAllByPartitaId(p.getId());
            d.setBigliettiVenduti(bl.stream().filter(b->b.getStato()==Biglietto.Stato.VALIDO).count());
            d.setIncasso(bl.stream().filter(b->b.getStato()==Biglietto.Stato.VALIDO).mapToDouble(b->b.getPrezzoPagato().doubleValue()).sum());
            return d;
        }).toList());
        return r;
    }
    @Transactional(readOnly=true)
    public StatisticheResponse.VenditaPartitaDto getStatistichePartita(Long id){
        Partita p=partitaRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Partita"));
        var d=new StatisticheResponse.VenditaPartitaDto();
        d.setPartitaId(p.getId());d.setSquadraCasa(p.getSquadraCasa().getNome());
        d.setSquadraOspite(p.getSquadraOspite().getNome());d.setDataOra(p.getDataOra().toString());d.setStato(p.getStato().name());
        var bl=bigliettoRepo.findAllByPartitaId(id);
        d.setBigliettiVenduti(bl.stream().filter(b->b.getStato()==Biglietto.Stato.VALIDO).count());
        d.setIncasso(bl.stream().filter(b->b.getStato()==Biglietto.Stato.VALIDO).mapToDouble(b->b.getPrezzoPagato().doubleValue()).sum());
        return d;
    }
}
