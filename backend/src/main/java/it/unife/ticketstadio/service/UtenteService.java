package it.unife.ticketstadio.service;

import it.unife.ticketstadio.dto.UtenteResponse;
import it.unife.ticketstadio.entity.Utente;
import it.unife.ticketstadio.exception.ResourceNotFoundException;
import it.unife.ticketstadio.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service per le operazioni sull'utente autenticato.
 */
@Service
@RequiredArgsConstructor
public class UtenteService {

    private final UtenteRepository utenteRepo;

    /**
     * Restituisce i dati del profilo dell'utente attualmente loggato.
     */
    @Transactional(readOnly = true)
    public UtenteResponse getMe(String email) {
        Utente u = utenteRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utente"));
        return UtenteResponse.from(u);
    }
}
