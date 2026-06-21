package it.unife.ticketstadio.repository;
import it.unife.ticketstadio.entity.Promozione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface PromozioneRepository extends JpaRepository<Promozione,Long> {
    Optional<Promozione> findByCodice(String codice);
}
