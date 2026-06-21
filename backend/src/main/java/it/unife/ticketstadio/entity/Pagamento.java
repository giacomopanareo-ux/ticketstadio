package it.unife.ticketstadio.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity @Table(name="pagamento")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Pagamento {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="biglietto_id",unique=true) private Biglietto biglietto;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Metodo metodo;
    @Column(nullable=false,precision=8,scale=2) private BigDecimal importo;
    @Column(nullable=false) private LocalDateTime data;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Stato stato;
    public enum Metodo{CARTA,PAYPAL,BONIFICO}
    public enum Stato{COMPLETATO,FALLITO,RIMBORSATO}
}
