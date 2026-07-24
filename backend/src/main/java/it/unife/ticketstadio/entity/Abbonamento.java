package it.unife.ticketstadio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity che rappresenta un abbonamento stagionale sulla tabella "abbonamento".
 * Il vincolo di unicità impedisce a un utente di avere due abbonamenti
 * per lo stesso settore nella stessa stagione.
 */
@Entity
@Table(name = "abbonamento",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"utente_id", "settore_id", "stagione"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Abbonamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Utente titolare dell'abbonamento (non serializzato nel JSON). */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utente_id")
    private Utente utente;

    /** Settore a cui dà accesso l'abbonamento. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settore_id")
    private Settore settore;

    /** Stagione di riferimento, es. "2024/2025" (max 9 caratteri). */
    @Column(nullable = false, length = 9)
    private String stagione;

    /** Periodo di validità dell'abbonamento. */
    @Column(nullable = false)
    private LocalDate dataInizio;

    @Column(nullable = false)
    private LocalDate dataFine;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal prezzo;

    /** L'abbonamento è attivo se oggi rientra nel periodo [dataInizio, dataFine]. */
    public boolean isAttivo() {
        LocalDate oggi = LocalDate.now();
        return !oggi.isBefore(dataInizio) && !oggi.isAfter(dataFine);
    }
}
