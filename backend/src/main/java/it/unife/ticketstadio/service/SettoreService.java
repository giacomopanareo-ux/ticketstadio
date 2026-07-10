package it.unife.ticketstadio.service;
import it.unife.ticketstadio.dto.SettoreRequest;
import it.unife.ticketstadio.dto.SettoreResponse;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.ResourceNotFoundException;
import it.unife.ticketstadio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;
@Service @RequiredArgsConstructor @Transactional
public class SettoreService {
    private final SettoreRepository settoreRepo;
    private final StadioRepository stadioRepo;
    private final PostoRepository postoRepo;
    private final SquadraRepository squadraRepo;
    @Transactional(readOnly=true) public List<SettoreResponse> getAll(){
        Map<Long,String> squadraPerStadio=new HashMap<>();
        for(Squadra sq:squadraRepo.findAll()){
            if(sq.getHomeStadium()!=null)
                squadraPerStadio.merge(sq.getHomeStadium().getId(),sq.getNome(),(a,b)->a+" / "+b);
        }
        return settoreRepo.findAll().stream()
            .map(s->SettoreResponse.from(s,s.getStadio()!=null?squadraPerStadio.get(s.getStadio().getId()):null))
            .collect(Collectors.toList());
    }
    @Transactional(readOnly=true) public List<Posto> getPosti(Long id){if(!settoreRepo.existsById(id))throw new ResourceNotFoundException("Settore");return postoRepo.findBySettoreId(id);}
    public Settore crea(SettoreRequest req){Stadio s=stadioRepo.findById(req.getStadioId()).orElseThrow(()->new ResourceNotFoundException("Stadio"));return settoreRepo.save(Settore.builder().nome(req.getNome()).capienza(req.getCapienza()).prezzoBase(req.getPrezzoBase()).stadio(s).build());}
    public Settore aggiorna(Long id,SettoreRequest req){Settore s=settoreRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Settore"));s.setNome(req.getNome());s.setCapienza(req.getCapienza());s.setPrezzoBase(req.getPrezzoBase());return settoreRepo.save(s);}
}
