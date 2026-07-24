package it.unife.ticketstadio.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.unife.ticketstadio.dto.SettoreRequest;
import it.unife.ticketstadio.dto.SettoreResponse;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.service.SettoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST per i settori dello stadio (path base "/api/settori").
 * Lettura pubblica; creazione e modifica riservate agli ADMIN.
 */
@RestController
@RequestMapping("/api/settori")
@RequiredArgsConstructor
public class SettoreController {

    private final SettoreService settoreService;

    /** GET /api/settori -> elenco di tutti i settori. */
    @GetMapping
    public ResponseEntity<List<SettoreResponse>> getAll() {
        return ResponseEntity.ok(settoreService.getAll());
    }

    /** GET /api/settori/{id}/posti -> posti appartenenti a un settore. */
    @GetMapping("/{id}/posti")
    public ResponseEntity<List<Posto>> getPosti(@PathVariable Long id) {
        return ResponseEntity.ok(settoreService.getPosti(id));
    }

    /** POST /api/settori -> crea un settore (solo ADMIN). Ritorna 201 Created. */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Settore> crea(@Valid @RequestBody SettoreRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(settoreService.crea(req));
    }

    /** PUT /api/settori/{id} -> aggiorna un settore (solo ADMIN). */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Settore> aggiorna(@PathVariable Long id,
                                            @Valid @RequestBody SettoreRequest req) {
        return ResponseEntity.ok(settoreService.aggiorna(id, req));
    }
}
