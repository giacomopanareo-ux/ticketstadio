package it.unife.ticketstadio.repository;

import it.unife.ticketstadio.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository per i pagamenti. Offre i soli metodi CRUD di base di JpaRepository.
 */
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
