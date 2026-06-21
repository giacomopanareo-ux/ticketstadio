package it.unife.ticketstadio.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.service.BigliettoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/biglietti") @RequiredArgsConstructor
public class BigliettoController {
    private final BigliettoService bigliettoService;
    @PostMapping("/acquista") @PreAuthorize("isAuthenticated()") @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<BigliettoResponse> acquista(@Valid @RequestBody AcquistoBigliettoRequest req,@AuthenticationPrincipal UserDetails user){return ResponseEntity.status(HttpStatus.CREATED).body(bigliettoService.acquista(req,user.getUsername()));}
    @GetMapping("/miei") @PreAuthorize("isAuthenticated()") @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<List<BigliettoResponse>> getMiei(@AuthenticationPrincipal UserDetails user){return ResponseEntity.ok(bigliettoService.getMiei(user.getUsername()));}
    @GetMapping("/{id}") @PreAuthorize("isAuthenticated()") @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<BigliettoResponse> getById(@PathVariable Long id,@AuthenticationPrincipal UserDetails user){return ResponseEntity.ok(bigliettoService.getById(id,user.getUsername()));}
}
