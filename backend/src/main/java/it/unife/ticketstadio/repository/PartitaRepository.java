package it.unife.ticketstadio.repository;

import it.unife.ticketstadio.entity.Partita;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository per le partite.
 */
@Repository
public interface PartitaRepository extends JpaRepository<Partita, Long> {

    /** Partite in un dato stato, ordinate per data/ora crescente. */
    List<Partita> findByStatoOrderByDataOraAsc(Partita.Stato stato);

    /** Partite ancora programmate, ordinate dalla più vicina. */
    @Query("SELECT p FROM Partita p WHERE p.stato='PROGRAMMATA' ORDER BY p.dataOra ASC")
    List<Partita> findProssime();
}
