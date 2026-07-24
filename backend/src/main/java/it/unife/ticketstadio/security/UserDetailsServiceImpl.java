package it.unife.ticketstadio.security;

import it.unife.ticketstadio.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * Implementazione di UserDetailsService: dice a Spring Security come caricare
 * un utente a partire dal suo "username" (nel nostro caso l'email).
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtenteRepository utenteRepository;

    /**
     * Cerca l'utente per email. Se non esiste, lancia l'eccezione standard di Spring Security.
     * (L'entità Utente implementa già UserDetails, quindi può essere restituita direttamente.)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return utenteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + email));
    }
}
