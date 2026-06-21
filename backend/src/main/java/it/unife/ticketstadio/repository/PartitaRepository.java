package it.unife.ticketstadio.repository;
import it.unife.ticketstadio.entity.Partita;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PartitaRepository extends JpaRepository<Partita,Long> {
    List<Partita> findByStatoOrderByDataOraAsc(Partita.Stato stato);
    @Query("SELECT p FROM Partita p WHERE p.stato='PROGRAMMATA' ORDER BY p.dataOra ASC")
    List<Partita> findProssime();
}
