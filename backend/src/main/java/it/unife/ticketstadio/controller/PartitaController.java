package it.unife.ticketstadio.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.repository.BigliettoRepository;
import it.unife.ticketstadio.service.PartitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller REST per le partite.
 * Espone gli endpoint sotto il path base "/api/partite".
 * Le operazioni di lettura sono pubbliche, quelle di modifica solo per gli ADMIN.
 */
@RestController
@RequestMapping("/api/partite")
@RequiredArgsConstructor
public class PartitaController {

    private final PartitaService partitaService;
    private final BigliettoRepository bigliettoRepo;

    /** GET /api/partite -> elenco di tutte le partite. */
    @GetMapping
    public ResponseEntity<List<PartitaResponse>> getAll() {
        return ResponseEntity.ok(partitaService.getAll());
    }

    /** GET /api/partite/{id} -> dettaglio di una partita. */
    @GetMapping("/{id}")
    public ResponseEntity<PartitaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(partitaService.getById(id));
    }

    /** GET /api/partite/{id}/posti -> id dei posti già occupati per quella partita. */
    @GetMapping("/{id}/posti")
    public ResponseEntity<Set<Long>> getPosti(@PathVariable Long id) {
        return ResponseEntity.ok(bigliettoRepo.findPostiOccupatiByPartita(id));
    }

    /** POST /api/partite -> crea una partita (solo ADMIN). Ritorna 201 Created. */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PartitaResponse> crea(@Valid @RequestBody PartitaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partitaService.crea(req));
    }

    /** PUT /api/partite/{id} -> aggiorna una partita (solo ADMIN). */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PartitaResponse> aggiorna(@PathVariable Long id,
                                                    @Valid @RequestBody PartitaRequest req) {
        return ResponseEntity.ok(partitaService.aggiorna(id, req));
    }

    /** PUT /api/partite/{id}/annulla -> annulla una partita (solo ADMIN). Ritorna 204 No Content. */
    @PutMapping("/{id}/annulla")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> annulla(@PathVariable Long id) {
        partitaService.annulla(id);
        return ResponseEntity.noContent().build();
    }
}
