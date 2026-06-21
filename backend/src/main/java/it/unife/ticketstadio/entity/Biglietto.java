package it.unife.ticketstadio.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="biglietto",uniqueConstraints={@UniqueConstraint(columnNames={"partita_id","posto_id"})})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Biglietto {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="partita_id") private Partita partita;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="posto_id") private Posto posto;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="utente_id") private Utente utente;
    @Column(nullable=false,precision=8,scale=2) private BigDecimal prezzoPagato;
    @Column(nullable=false) private LocalDateTime dataAcquisto;
    @Enumerated(EnumType.STRING) @Column(nullable=false) @Builder.Default private Stato stato=Stato.VALIDO;
    @OneToOne(mappedBy="biglietto",cascade=CascadeType.ALL) private Pagamento pagamento;
    public enum Stato{VALIDO,USATO,ANNULLATO}
}
