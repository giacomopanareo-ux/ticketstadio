package it.unife.ticketstadio.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import it.unife.ticketstadio.dto.StatisticheResponse;
import it.unife.ticketstadio.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admin") @RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") @SecurityRequirement(name="bearerAuth")
public class AdminController {
    private final AdminService adminService;
    @GetMapping("/statistiche/vendite") public ResponseEntity<StatisticheResponse> getVendite(){return ResponseEntity.ok(adminService.getStatisticheVendite());}
    @GetMapping("/statistiche/partita/{id}") public ResponseEntity<StatisticheResponse.VenditaPartitaDto> getPartita(@PathVariable Long id){return ResponseEntity.ok(adminService.getStatistichePartita(id));}
}
