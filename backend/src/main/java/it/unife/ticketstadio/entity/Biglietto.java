package it.unife.ticketstadio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity che rappresenta un biglietto sulla tabella "biglietto".
 *
 * Il vincolo di unicità su (partita_id, posto_id) garantisce a livello di database
 * che lo stesso posto non possa essere venduto due volte per la stessa partita.
 */
@Entity
@Table(name = "biglietto",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"partita_id", "posto_id"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Biglietto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazioni "molti-a-uno": molti biglietti appartengono a una partita, un posto, un utente.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partita_id")
    private Partita partita;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "posto_id")
    private Posto posto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utente_id")
    private Utente utente;

    /** Prezzo effettivamente pagato (può differire dal prezzo base se c'è stata una promo). */
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal prezzoPagato;

    @Column(nullable = false)
    private LocalDateTime dataAcquisto;

    /**
     * Stato del biglietto, salvato come stringa nel DB (@Enumerated(STRING)).
     * @Builder.Default -> se non specificato nel builder, il valore iniziale è VALIDO.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Stato stato = Stato.VALIDO;

    /**
     * Pagamento associato al biglietto (relazione uno-a-uno).
     * cascade = ALL -> salvando/eliminando il biglietto si propaga anche al pagamento.
     */
    @OneToOne(mappedBy = "biglietto", cascade = CascadeType.ALL)
    private Pagamento pagamento;

    /** Stati possibili di un biglietto. */
    public enum Stato { VALIDO, USATO, ANNULLATO }
}
