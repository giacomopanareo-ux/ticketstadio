package it.unife.ticketstadio.repository;

import it.unife.ticketstadio.entity.Settore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository per i settori. Offre i soli metodi CRUD di base di JpaRepository.
 */
@Repository
public interface SettoreRepository extends JpaRepository<Settore, Long> {
}
