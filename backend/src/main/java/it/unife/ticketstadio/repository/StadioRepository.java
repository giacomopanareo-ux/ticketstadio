package it.unife.ticketstadio.repository;

import it.unife.ticketstadio.entity.Stadio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository per gli stadi. Offre i soli metodi CRUD di base di JpaRepository.
 */
@Repository
public interface StadioRepository extends JpaRepository<Stadio, Long> {
}
