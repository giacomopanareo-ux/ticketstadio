package it.unife.ticketstadio.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.unife.ticketstadio.dto.SettoreRequest;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.service.SettoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/settori") @RequiredArgsConstructor
public class SettoreController {
    private final SettoreService settoreService;
    @GetMapping public ResponseEntity<List<Settore>> getAll(){return ResponseEntity.ok(settoreService.getAll());}
    @GetMapping("/{id}/posti") public ResponseEntity<List<Posto>> getPosti(@PathVariable Long id){return ResponseEntity.ok(settoreService.getPosti(id));}
    @PostMapping @PreAuthorize("hasRole('ADMIN')") @SecurityRequirement(name="bearerAuth") public ResponseEntity<Settore> crea(@Valid @RequestBody SettoreRequest req){return ResponseEntity.status(HttpStatus.CREATED).body(settoreService.crea(req));}
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @SecurityRequirement(name="bearerAuth") public ResponseEntity<Settore> aggiorna(@PathVariable Long id,@Valid @RequestBody SettoreRequest req){return ResponseEntity.ok(settoreService.aggiorna(id,req));}
}
