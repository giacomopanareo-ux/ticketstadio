package it.unife.ticketstadio.repository;
import it.unife.ticketstadio.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface AbbonamentoRepository extends JpaRepository<Abbonamento,Long> {
    boolean existsByUtenteAndSettoreAndStagione(Utente u,Settore s,String stagione);
    List<Abbonamento> findByUtenteEmail(String email);
}
