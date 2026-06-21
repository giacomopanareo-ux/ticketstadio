package it.unife.ticketstadio.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity @Table(name="abbonamento",uniqueConstraints={@UniqueConstraint(columnNames={"utente_id","settore_id","stagione"})})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Abbonamento {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @JsonIgnore @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="utente_id") private Utente utente;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="settore_id") private Settore settore;
    @Column(nullable=false,length=9) private String stagione;
    @Column(nullable=false) private LocalDate dataInizio;
    @Column(nullable=false) private LocalDate dataFine;
    @Column(nullable=false,precision=8,scale=2) private BigDecimal prezzo;
    public boolean isAttivo(){LocalDate o=LocalDate.now();return !o.isBefore(dataInizio)&&!o.isAfter(dataFine);}
}
