package it.unife.ticketstadio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity che rappresenta il pagamento di un biglietto sulla tabella "pagamento".
 * Ogni pagamento è collegato a un solo biglietto (relazione uno-a-uno).
 */
@Entity
@Table(name = "pagamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Biglietto pagato. unique = true garantisce un solo pagamento per biglietto. */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "biglietto_id", unique = true)
    private Biglietto biglietto;

    /** Metodo di pagamento usato (salvato come stringa). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Metodo metodo;

    /** Importo pagato. */
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal importo;

    @Column(nullable = false)
    private LocalDateTime data;

    /** Esito del pagamento (salvato come stringa). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Stato stato;

    /** Metodi di pagamento accettati. */
    public enum Metodo { CARTA, PAYPAL, BONIFICO }

    /** Stati possibili di un pagamento. */
    public enum Stato { COMPLETATO, FALLITO, RIMBORSATO }
}
