package it.unife.ticketstadio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity che rappresenta una partita sulla tabella "partita".
 */
@Entity
@Table(name = "partita")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Squadra che gioca in casa. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "squadra_casa_id")
    private Squadra squadraCasa;

    /** Squadra ospite. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "squadra_ospite_id")
    private Squadra squadraOspite;

    /** Data e ora di inizio della partita. */
    @Column(nullable = false)
    private LocalDateTime dataOra;

    /** Stadio in cui si gioca. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stadio_id")
    private Stadio stadio;

    /** Stato della partita; di default una nuova partita è PROGRAMMATA. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Stato stato = Stato.PROGRAMMATA;

    /** Si possono comprare biglietti solo se la partita è ancora PROGRAMMATA. */
    public boolean isAcquistabile() {
        return stato == Stato.PROGRAMMATA;
    }

    /** Stati possibili di una partita. */
    public enum Stato { PROGRAMMATA, IN_CORSO, CONCLUSA, ANNULLATA }
}
