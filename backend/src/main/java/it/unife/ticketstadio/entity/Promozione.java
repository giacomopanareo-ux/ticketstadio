package it.unife.ticketstadio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entity che rappresenta una promozione (codice sconto) sulla tabella "promozione".
 *
 * Annotazioni Lombok:
 * - @Data              -> genera getter, setter, toString, equals/hashCode.
 * - @NoArgsConstructor / @AllArgsConstructor -> costruttori vuoto e completo (richiesti da JPA/Builder).
 * - @Builder           -> permette di creare l'oggetto con la sintassi .builder()...build().
 */
@Entity
@Table(name = "promozione", uniqueConstraints = @UniqueConstraint(columnNames = "codice"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promozione {

    /** Chiave primaria, generata automaticamente dal database. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Codice della promozione (univoco, max 20 caratteri), es. "ESTATE25". */
    @Column(nullable = false, unique = true, length = 20)
    private String codice;

    private String descrizione;

    /** Percentuale di sconto (es. 25.00 = -25%). precision/scale definiscono le cifre memorizzabili. */
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal scontoPercentuale;

    /** Periodo di validità della promozione. */
    @Column(nullable = false)
    private LocalDate dataInizio;

    @Column(nullable = false)
    private LocalDate dataFine;

    /**
     * Partita a cui la promo è legata (opzionale).
     * fetch = LAZY -> caricata dal DB solo quando serve, non subito.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partita_id")
    private Partita partita;

    /**
     * Verifica se la promozione è valida oggi e applicabile alla partita indicata.
     * È valida se: oggi rientra nel periodo [dataInizio, dataFine] E
     * la promo è generica (partita == null) oppure è proprio quella partita.
     */
    public boolean isValida(Partita p) {
        LocalDate oggi = LocalDate.now();
        boolean nelPeriodo = !oggi.isBefore(dataInizio) && !oggi.isAfter(dataFine);
        boolean partitaCompatibile = (partita == null) || partita.getId().equals(p.getId());
        return nelPeriodo && partitaCompatibile;
    }
}
