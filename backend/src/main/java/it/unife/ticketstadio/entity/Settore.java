package it.unife.ticketstadio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Entity che rappresenta un settore dello stadio (es. "Curva Nord", "Tribuna").
 * Il nome è univoco all'interno dello stesso stadio.
 */
@Entity
@Table(name = "settore",
       uniqueConstraints = @UniqueConstraint(columnNames = {"stadio_id", "nome"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    /** Numero di posti disponibili nel settore. */
    @Column(nullable = false)
    private Integer capienza;

    /** Prezzo base del biglietto per questo settore. */
    @Column(nullable = false)
    private BigDecimal prezzoBase;

    /**
     * Stadio a cui appartiene il settore.
     * @JsonIgnore -> non serializzato nel JSON (evita loop infiniti e dati inutili nelle risposte).
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stadio_id")
    private Stadio stadio;

    /** Posti contenuti nel settore (relazione uno-a-molti). */
    @JsonIgnore
    @OneToMany(mappedBy = "settore", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Posto> posti = new ArrayList<>();
}
