package it.unife.ticketstadio.repository;
import it.unife.ticketstadio.entity.Squadra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SquadraRepository extends JpaRepository<Squadra,Long> {}
