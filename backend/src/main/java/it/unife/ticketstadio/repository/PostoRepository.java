package it.unife.ticketstadio.repository;

import it.unife.ticketstadio.entity.Posto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository per i posti.
 */
@Repository
public interface PostoRepository extends JpaRepository<Posto, Long> {

    /** Tutti i posti appartenenti a un settore. */
    List<Posto> findBySettoreId(Long settoreId);
}
