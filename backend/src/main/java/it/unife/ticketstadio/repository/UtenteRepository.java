package it.unife.ticketstadio.repository;

import it.unife.ticketstadio.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository per gli utenti.
 */
@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

    /** Cerca un utente dalla sua email (usato per login e recupero profilo). */
    Optional<Utente> findByEmail(String email);

    /** Verifica se esiste già un utente con quella email (usato in registrazione). */
    boolean existsByEmail(String email);
}
