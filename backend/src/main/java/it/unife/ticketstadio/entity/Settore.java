package it.unife.ticketstadio.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.*;
@Entity @Table(name="settore",uniqueConstraints=@UniqueConstraint(columnNames={"stadio_id","nome"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Settore {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String nome;
    @Column(nullable=false) private Integer capienza;
    @Column(nullable=false) private BigDecimal prezzoBase;
    @JsonIgnore @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="stadio_id") private Stadio stadio;
    @JsonIgnore @OneToMany(mappedBy="settore",cascade=CascadeType.ALL) @Builder.Default private List<Posto> posti=new ArrayList<>();
}
