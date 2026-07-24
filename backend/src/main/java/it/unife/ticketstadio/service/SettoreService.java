package it.unife.ticketstadio.service;

import it.unife.ticketstadio.dto.SettoreRequest;
import it.unife.ticketstadio.dto.SettoreResponse;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.ResourceNotFoundException;
import it.unife.ticketstadio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service che gestisce i settori dello stadio: elenco, posti,
 * creazione e aggiornamento.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SettoreService {

    private final SettoreRepository settoreRepo;
    private final StadioRepository stadioRepo;
    private final PostoRepository postoRepo;
    private final SquadraRepository squadraRepo;

    /**
     * Elenco di tutti i settori, arricchito con il nome della squadra che gioca in quello stadio.
     */
    @Transactional(readOnly = true)
    public List<SettoreResponse> getAll() {
        // Costruisco una mappa "id stadio -> nome/i squadra/e" che ci gioca in casa.
        // Se più squadre condividono lo stadio, i nomi vengono concatenati con " / ".
        Map<Long, String> squadraPerStadio = new HashMap<>();
        for (Squadra sq : squadraRepo.findAll()) {
            if (sq.getHomeStadium() != null) {
                squadraPerStadio.merge(
                        sq.getHomeStadium().getId(),
                        sq.getNome(),
                        (a, b) -> a + " / " + b);
            }
        }

        // Converto ogni settore in DTO, aggiungendo la squadra associata al suo stadio.
        return settoreRepo.findAll().stream()
                .map(s -> SettoreResponse.from(
                        s,
                        s.getStadio() != null ? squadraPerStadio.get(s.getStadio().getId()) : null))
                .collect(Collectors.toList());
    }

    /**
     * Restituisce i posti di un settore. Se il settore non esiste, lancia 404.
     */
    @Transactional(readOnly = true)
    public List<Posto> getPosti(Long id) {
        if (!settoreRepo.existsById(id)) {
            throw new ResourceNotFoundException("Settore");
        }
        return postoRepo.findBySettoreId(id);
    }

    /**
     * Crea un nuovo settore collegato a uno stadio esistente.
     */
    public Settore crea(SettoreRequest req) {
        Stadio s = stadioRepo.findById(req.getStadioId())
                .orElseThrow(() -> new ResourceNotFoundException("Stadio"));
        return settoreRepo.save(
                Settore.builder()
                        .nome(req.getNome())
                        .capienza(req.getCapienza())
                        .prezzoBase(req.getPrezzoBase())
                        .stadio(s)
                        .build());
    }

    /**
     * Aggiorna nome, capienza e prezzo base di un settore esistente.
     */
    public Settore aggiorna(Long id, SettoreRequest req) {
        Settore s = settoreRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Settore"));
        s.setNome(req.getNome());
        s.setCapienza(req.getCapienza());
        s.setPrezzoBase(req.getPrezzoBase());
        return settoreRepo.save(s);
    }
}
