package it.unife.ticketstadio.repository;
import it.unife.ticketstadio.entity.Posto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PostoRepository extends JpaRepository<Posto,Long> {
    List<Posto> findBySettoreId(Long settoreId);
}
