package it.unife.ticketstadio.repository;
import it.unife.ticketstadio.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface BigliettoRepository extends JpaRepository<Biglietto,Long> {
    boolean existsByPartitaAndPosto(Partita partita, Posto posto);
    List<Biglietto> findByUtenteEmail(String email);
    List<Biglietto> findAllByPartitaId(Long partitaId);
    @Query("SELECT b.posto.id FROM Biglietto b WHERE b.partita.id=:pid AND b.stato<>'ANNULLATO'")
    Set<Long> findPostiOccupatiByPartita(@Param("pid") Long partitaId);
    @Query("SELECT COUNT(b) FROM Biglietto b WHERE b.stato='VALIDO'") long countValidi();
    @Query("SELECT COALESCE(SUM(b.prezzoPagato),0) FROM Biglietto b WHERE b.stato='VALIDO'") double sumIncasso();
}
