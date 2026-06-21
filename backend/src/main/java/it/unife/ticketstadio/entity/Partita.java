package it.unife.ticketstadio.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="partita")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Partita {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="squadra_casa_id") private Squadra squadraCasa;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="squadra_ospite_id") private Squadra squadraOspite;
    @Column(nullable=false) private LocalDateTime dataOra;
    @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="stadio_id") private Stadio stadio;
    @Enumerated(EnumType.STRING) @Column(nullable=false) @Builder.Default private Stato stato=Stato.PROGRAMMATA;
    public boolean isAcquistabile(){return stato==Stato.PROGRAMMATA;}
    public enum Stato{PROGRAMMATA,IN_CORSO,CONCLUSA,ANNULLATA}
}
