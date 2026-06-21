package it.unife.ticketstadio.security;
import it.unife.ticketstadio.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service @RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UtenteRepository utenteRepository;
    @Override
    public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException{
        return utenteRepository.findByEmail(email)
            .orElseThrow(()->new UsernameNotFoundException("Utente non trovato: "+email));
    }
}
