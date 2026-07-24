package it.unife.ticketstadio.repository;

import it.unife.ticketstadio.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Repository per i biglietti.
 * Estendendo JpaRepository si ottengono gratis i metodi CRUD di base
 * (save, findById, findAll, delete...). Qui aggiungiamo query specifiche:
 * i metodi con nomi "parlanti" (findBy..., existsBy...) vengono generati da Spring Data,
 * mentre quelli con @Query usano una query JPQL scritta a mano.
 */
@Repository
public interface BigliettoRepository extends JpaRepository<Biglietto, Long> {

    /** Verifica se esiste già un biglietto per quella coppia partita + posto. */
    boolean existsByPartitaAndPosto(Partita partita, Posto posto);

    /** Biglietti di un utente, cercati tramite la sua email. */
    List<Biglietto> findByUtenteEmail(String email);

    /** Tutti i biglietti di una partita. */
    List<Biglietto> findAllByPartitaId(Long partitaId);

    /** Id dei posti occupati (non annullati) per una partita: usato per la mappa dei posti. */
    @Query("SELECT b.posto.id FROM Biglietto b WHERE b.partita.id=:pid AND b.stato<>'ANNULLATO'")
    Set<Long> findPostiOccupatiByPartita(@Param("pid") Long partitaId);

    /** Conteggio dei biglietti validi (per le statistiche). */
    @Query("SELECT COUNT(b) FROM Biglietto b WHERE b.stato='VALIDO'")
    long countValidi();

    /** Somma degli incassi dai biglietti validi (COALESCE evita null se non ce ne sono). */
    @Query("SELECT COALESCE(SUM(b.prezzoPagato),0) FROM Biglietto b WHERE b.stato='VALIDO'")
    double sumIncasso();
}
