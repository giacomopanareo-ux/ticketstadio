package it.unife.ticketstadio.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="squadra",uniqueConstraints=@UniqueConstraint(columnNames="nome"))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Squadra {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,unique=true) private String nome;
    @Column(nullable=false) private String citta;
    private String logoUrl;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="home_stadium_id") private Stadio homeStadium;
}
