package it.unife.ticketstadio.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="posto",uniqueConstraints=@UniqueConstraint(columnNames={"settore_id","fila","numero"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Posto {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,length=2) private String fila;
    @Column(nullable=false) private Integer numero;
    @JsonIgnore @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="settore_id") private Settore settore;
}
