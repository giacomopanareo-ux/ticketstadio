package it.unife.ticketstadio.repository;

import it.unife.ticketstadio.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository per gli abbonamenti.
 */
@Repository
public interface AbbonamentoRepository extends JpaRepository<Abbonamento, Long> {

    /** Verifica se l'utente ha già un abbonamento per quel settore e stagione. */
    boolean existsByUtenteAndSettoreAndStagione(Utente u, Settore s, String stagione);

    /** Abbonamenti di un utente, cercati tramite la sua email. */
    List<Abbonamento> findByUtenteEmail(String email);
}
