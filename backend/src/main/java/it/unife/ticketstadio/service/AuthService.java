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
@Service @RequiredArgsConstructor
public class AuthService {
    private final UtenteRepository utenteRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService uds;
    public void register(RegisterRequest req){
        if(utenteRepo.existsByEmail(req.getEmail())) throw new IllegalArgumentException("Email già registrata");
        utenteRepo.save(Utente.builder().nome(req.getNome()).cognome(req.getCognome())
            .email(req.getEmail()).passwordHash(encoder.encode(req.getPassword()))
            .dataRegistrazione(LocalDate.now()).build());
    }
    public String login(LoginRequest req){
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(),req.getPassword()));
        return jwtUtil.generateToken(uds.loadUserByUsername(req.getEmail()));
    }
}
