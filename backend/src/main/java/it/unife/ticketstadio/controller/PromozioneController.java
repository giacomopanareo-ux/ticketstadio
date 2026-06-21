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
@RestController @RequestMapping("/api/promozioni") @RequiredArgsConstructor
public class PromozioneController {
    private final PromozioneService promoService;
    @PostMapping("/valida") @PreAuthorize("isAuthenticated()") @SecurityRequirement(name="bearerAuth") public ResponseEntity<Promozione> valida(@Valid @RequestBody ValidaPromoRequest req){return ResponseEntity.ok(promoService.valida(req));}
    @GetMapping @PreAuthorize("hasRole('ADMIN')") @SecurityRequirement(name="bearerAuth") public ResponseEntity<List<Promozione>> getAll(){return ResponseEntity.ok(promoService.getAll());}
    @PostMapping @PreAuthorize("hasRole('ADMIN')") @SecurityRequirement(name="bearerAuth") public ResponseEntity<Promozione> crea(@Valid @RequestBody PromozioneRequest req){return ResponseEntity.status(HttpStatus.CREATED).body(promoService.crea(req));}
    @PutMapping("/{id}/disattiva") @PreAuthorize("hasRole('ADMIN')") @SecurityRequirement(name="bearerAuth") public ResponseEntity<Void> disattiva(@PathVariable Long id){promoService.disattiva(id);return ResponseEntity.noContent().build();}
}
