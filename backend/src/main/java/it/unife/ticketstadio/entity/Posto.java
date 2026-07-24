package it.unife.ticketstadio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity che rappresenta un singolo posto a sedere sulla tabella "posto".
 * Un posto è identificato univocamente da settore + fila + numero.
 */
@Entity
@Table(name = "posto",
       uniqueConstraints = @UniqueConstraint(columnNames = {"settore_id", "fila", "numero"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Posto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fila del posto (es. "A", "B"), massimo 2 caratteri. */
    @Column(nullable = false, length = 2)
    private String fila;

    /** Numero del posto all'interno della fila. */
    @Column(nullable = false)
    private Integer numero;

    /** Settore a cui appartiene il posto (non serializzato nel JSON). */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settore_id")
    private Settore settore;
}
