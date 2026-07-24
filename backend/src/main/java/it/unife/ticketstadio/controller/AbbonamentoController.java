package it.unife.ticketstadio.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.unife.ticketstadio.dto.AbbonamentoRequest;
import it.unife.ticketstadio.entity.Abbonamento;
import it.unife.ticketstadio.service.AbbonamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST per gli abbonamenti (path base "/api/abbonamenti").
 * Tutti gli endpoint richiedono un utente autenticato.
 *
 * Nota: @AuthenticationPrincipal inietta l'utente loggato; user.getUsername()
 * corrisponde alla sua email, che passiamo al service.
 */
@RestController
@RequestMapping("/api/abbonamenti")
@RequiredArgsConstructor
public class AbbonamentoController {

    private final AbbonamentoService abbService;

    /** POST /api/abbonamenti -> sottoscrive un nuovo abbonamento. Ritorna 201 Created. */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Abbonamento> sottoscrivi(@Valid @RequestBody AbbonamentoRequest req,
                                                   @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(abbService.sottoscrivi(req, user.getUsername()));
    }

    /** GET /api/abbonamenti/miei -> abbonamenti dell'utente loggato. */
    @GetMapping("/miei")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Abbonamento>> getMiei(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(abbService.getMiei(user.getUsername()));
    }

    /** PUT /api/abbonamenti/{id}/rinnova -> rinnova un abbonamento dell'utente. */
    @PutMapping("/{id}/rinnova")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Abbonamento> rinnova(@PathVariable Long id,
                                               @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(abbService.rinnova(id, user.getUsername()));
    }
}
