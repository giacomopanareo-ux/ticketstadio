package it.unife.ticketstadio.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.entity.Promozione;
import it.unife.ticketstadio.service.PromozioneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST per le promozioni (path base "/api/promozioni").
 * La validazione di un codice è per utenti autenticati; la gestione (CRUD) è solo ADMIN.
 */
@RestController
@RequestMapping("/api/promozioni")
@RequiredArgsConstructor
public class PromozioneController {

    private final PromozioneService promoService;

    /** POST /api/promozioni/valida -> controlla se un codice promo è valido per una partita. */
    @PostMapping("/valida")
    @PreAuthorize("isAuthenticated()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Promozione> valida(@Valid @RequestBody ValidaPromoRequest req) {
        return ResponseEntity.ok(promoService.valida(req));
    }

    /** GET /api/promozioni -> elenco di tutte le promozioni (solo ADMIN). */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<Promozione>> getAll() {
        return ResponseEntity.ok(promoService.getAll());
    }

    /** POST /api/promozioni -> crea una nuova promozione (solo ADMIN). Ritorna 201 Created. */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Promozione> crea(@Valid @RequestBody PromozioneRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promoService.crea(req));
    }

    /** PUT /api/promozioni/{id}/disattiva -> disattiva una promozione (solo ADMIN). Ritorna 204. */
    @PutMapping("/{id}/disattiva")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> disattiva(@PathVariable Long id) {
        promoService.disattiva(id);
        return ResponseEntity.noContent().build();
    }
}
