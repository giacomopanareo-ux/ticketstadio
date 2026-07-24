package it.unife.ticketstadio.service;

import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.entity.Utente;
import it.unife.ticketstadio.repository.UtenteRepository;
import it.unife.ticketstadio.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service che gestisce autenticazione e registrazione degli utenti.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtenteRepository utenteRepo;
    private final PasswordEncoder encoder;          // codifica le password (es. BCrypt)
    private final AuthenticationManager authManager; // verifica le credenziali di login
    private final JwtUtil jwtUtil;                   // genera i token JWT
    private final UserDetailsService uds;            // carica i dati utente per Spring Security

    /**
     * Registra un nuovo utente.
     * La password non viene mai salvata in chiaro: viene salvato solo il suo hash.
     */
    public void register(RegisterRequest req) {
        // Email univoca: se esiste già blocco la registrazione.
        if (utenteRepo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email già registrata");
        }

        utenteRepo.save(
                Utente.builder()
                        .nome(req.getNome())
                        .cognome(req.getCognome())
                        .email(req.getEmail())
                        .passwordHash(encoder.encode(req.getPassword())) // hashing della password
                        .dataRegistrazione(LocalDate.now())
                        .build());
    }

    /**
     * Esegue il login e restituisce il token JWT da usare nelle richieste successive.
     */
    public String login(LoginRequest req) {
        // Verifica email + password; se sbagliate lancia un'eccezione di autenticazione.
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        // Credenziali corrette -> genero e restituisco il token.
        return jwtUtil.generateToken(uds.loadUserByUsername(req.getEmail()));
    }
}
