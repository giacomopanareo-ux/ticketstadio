package it.unife.ticketstadio.controller;

import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller REST per l'autenticazione (path base "/api/auth").
 * Questi endpoint sono pubblici: servono proprio a registrarsi e a fare login.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** POST /api/auth/register -> registra un nuovo utente. Ritorna 201 Created. */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registrazione completata");
    }

    /** POST /api/auth/login -> esegue il login e restituisce il token JWT in un JSON {"token": "..."}. */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(Map.of("token", authService.login(req)));
    }
}
