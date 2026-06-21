package it.unife.ticketstadio.repository;
import it.unife.ticketstadio.entity.Stadio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface StadioRepository extends JpaRepository<Stadio,Long> {}
