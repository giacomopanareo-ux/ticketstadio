package it.unife.ticketstadio.controller;
import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register") public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest req){authService.register(req);return ResponseEntity.status(HttpStatus.CREATED).body("Registrazione completata");}
    @PostMapping("/login") public ResponseEntity<Map<String,String>> login(@Valid @RequestBody LoginRequest req){return ResponseEntity.ok(Map.of("token",authService.login(req)));}
}
