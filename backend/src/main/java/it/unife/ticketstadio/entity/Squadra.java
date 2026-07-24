package it.unife.ticketstadio.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity che rappresenta una squadra sulla tabella "squadra".
 * Il nome è univoco.
 */
@Entity
@Table(name = "squadra", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Squadra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String citta;

    /** URL del logo della squadra (opzionale). */
    private String logoUrl;

    /** Stadio in cui la squadra gioca le partite casalinghe (opzionale). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_stadium_id")
    private Stadio homeStadium;
}
