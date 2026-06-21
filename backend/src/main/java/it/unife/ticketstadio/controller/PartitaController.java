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
@RestController @RequestMapping("/api/partite") @RequiredArgsConstructor
public class PartitaController {
    private final PartitaService partitaService;
    private final BigliettoRepository bigliettoRepo;
    @GetMapping public ResponseEntity<List<PartitaResponse>> getAll(){return ResponseEntity.ok(partitaService.getAll());}
    @GetMapping("/{id}") public ResponseEntity<PartitaResponse> getById(@PathVariable Long id){return ResponseEntity.ok(partitaService.getById(id));}
    @GetMapping("/{id}/posti") public ResponseEntity<Set<Long>> getPosti(@PathVariable Long id){return ResponseEntity.ok(bigliettoRepo.findPostiOccupatiByPartita(id));}
    @PostMapping @PreAuthorize("hasRole('ADMIN')") @SecurityRequirement(name="bearerAuth") public ResponseEntity<PartitaResponse> crea(@Valid @RequestBody PartitaRequest req){return ResponseEntity.status(HttpStatus.CREATED).body(partitaService.crea(req));}
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @SecurityRequirement(name="bearerAuth") public ResponseEntity<PartitaResponse> aggiorna(@PathVariable Long id,@Valid @RequestBody PartitaRequest req){return ResponseEntity.ok(partitaService.aggiorna(id,req));}
    @PutMapping("/{id}/annulla") @PreAuthorize("hasRole('ADMIN')") @SecurityRequirement(name="bearerAuth") public ResponseEntity<Void> annulla(@PathVariable Long id){partitaService.annulla(id);return ResponseEntity.noContent().build();}
}
