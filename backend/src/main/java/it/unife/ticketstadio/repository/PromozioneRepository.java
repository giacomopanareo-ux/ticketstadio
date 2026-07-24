package it.unife.ticketstadio.repository;

import it.unife.ticketstadio.entity.Promozione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository per le promozioni.
 */
@Repository
public interface PromozioneRepository extends JpaRepository<Promozione, Long> {

    /** Cerca una promozione dal suo codice. Optional vuoto se non esiste. */
    Optional<Promozione> findByCodice(String codice);
}
