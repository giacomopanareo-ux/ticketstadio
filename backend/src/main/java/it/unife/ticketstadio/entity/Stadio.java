package it.unife.ticketstadio.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;
@Entity @Table(name="stadio",uniqueConstraints=@UniqueConstraint(columnNames="nome"))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Stadio {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,unique=true) private String nome;
    @Column(nullable=false) private String citta;
    @Column(nullable=false) private Integer capienzaTotale;
    @JsonIgnore @OneToMany(mappedBy="stadio",cascade=CascadeType.ALL) @Builder.Default private List<Settore> settori=new ArrayList<>();
}
