package it.unife.ticketstadio.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity @Table(name="promozione",uniqueConstraints=@UniqueConstraint(columnNames="codice"))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Promozione {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,unique=true,length=20) private String codice;
    private String descrizione;
    @Column(nullable=false,precision=5,scale=2) private BigDecimal scontoPercentuale;
    @Column(nullable=false) private LocalDate dataInizio;
    @Column(nullable=false) private LocalDate dataFine;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="partita_id") private Partita partita;
    public boolean isValida(Partita p){
        LocalDate o=LocalDate.now();
        return !o.isBefore(dataInizio)&&!o.isAfter(dataFine)&&(partita==null||partita.getId().equals(p.getId()));
    }
}
