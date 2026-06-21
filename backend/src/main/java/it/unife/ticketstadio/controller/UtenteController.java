package it.unife.ticketstadio.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.unife.ticketstadio.dto.UtenteResponse;
import it.unife.ticketstadio.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/utenti") @RequiredArgsConstructor
public class UtenteController {
    private final UtenteService utenteService;
    @GetMapping("/me") @PreAuthorize("isAuthenticated()") @SecurityRequirement(name="bearerAuth")
    public ResponseEntity<UtenteResponse> getMe(@AuthenticationPrincipal UserDetails user){return ResponseEntity.ok(utenteService.getMe(user.getUsername()));}
}
