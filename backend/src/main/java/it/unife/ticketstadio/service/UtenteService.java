package it.unife.ticketstadio.service;
import it.unife.ticketstadio.dto.UtenteResponse;
import it.unife.ticketstadio.exception.ResourceNotFoundException;
import it.unife.ticketstadio.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @RequiredArgsConstructor
public class UtenteService {
    private final UtenteRepository utenteRepo;
    @Transactional(readOnly=true)
    public UtenteResponse getMe(String email){return UtenteResponse.from(utenteRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Utente")));}
}
