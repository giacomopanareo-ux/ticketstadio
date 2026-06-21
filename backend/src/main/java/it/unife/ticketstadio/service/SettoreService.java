package it.unife.ticketstadio.service;
import it.unife.ticketstadio.dto.SettoreRequest;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.ResourceNotFoundException;
import it.unife.ticketstadio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional
public class SettoreService {
    private final SettoreRepository settoreRepo;
    private final StadioRepository stadioRepo;
    private final PostoRepository postoRepo;
    @Transactional(readOnly=true) public List<Settore> getAll(){return settoreRepo.findAll();}
    @Transactional(readOnly=true) public List<Posto> getPosti(Long id){if(!settoreRepo.existsById(id))throw new ResourceNotFoundException("Settore");return postoRepo.findBySettoreId(id);}
    public Settore crea(SettoreRequest req){Stadio s=stadioRepo.findById(req.getStadioId()).orElseThrow(()->new ResourceNotFoundException("Stadio"));return settoreRepo.save(Settore.builder().nome(req.getNome()).capienza(req.getCapienza()).prezzoBase(req.getPrezzoBase()).stadio(s).build());}
    public Settore aggiorna(Long id,SettoreRequest req){Settore s=settoreRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Settore"));s.setNome(req.getNome());s.setCapienza(req.getCapienza());s.setPrezzoBase(req.getPrezzoBase());return settoreRepo.save(s);}
}
