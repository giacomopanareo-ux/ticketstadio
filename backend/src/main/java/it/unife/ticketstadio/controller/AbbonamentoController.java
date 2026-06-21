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
@RestController @RequestMapping("/api/abbonamenti") @RequiredArgsConstructor
public class AbbonamentoController {
    private final AbbonamentoService abbService;
    @PostMapping @PreAuthorize("isAuthenticated()") @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<Abbonamento> sottoscrivi(@Valid @RequestBody AbbonamentoRequest req,@AuthenticationPrincipal UserDetails user){return ResponseEntity.status(HttpStatus.CREATED).body(abbService.sottoscrivi(req,user.getUsername()));}
    @GetMapping("/miei") @PreAuthorize("isAuthenticated()") @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<List<Abbonamento>> getMiei(@AuthenticationPrincipal UserDetails user){return ResponseEntity.ok(abbService.getMiei(user.getUsername()));}
    @PutMapping("/{id}/rinnova") @PreAuthorize("isAuthenticated()") @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<Abbonamento> rinnova(@PathVariable Long id,@AuthenticationPrincipal UserDetails user){return ResponseEntity.ok(abbService.rinnova(id,user.getUsername()));}
}
