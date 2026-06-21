package it.unife.ticketstadio.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDate;
import java.util.*;
@Entity @Table(name="utente",uniqueConstraints=@UniqueConstraint(columnNames="email"))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Utente implements UserDetails {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String nome;
    @Column(nullable=false) private String cognome;
    @Column(nullable=false,unique=true) private String email;
    @JsonIgnore @Column(nullable=false) private String passwordHash;
    @Enumerated(EnumType.STRING) @Column(nullable=false) @Builder.Default private Ruolo ruolo=Ruolo.TIFOSO;
    @Column(nullable=false) private LocalDate dataRegistrazione;
    @JsonIgnore @OneToMany(mappedBy="utente",cascade=CascadeType.ALL) @Builder.Default private List<Biglietto> biglietti=new ArrayList<>();
    @JsonIgnore @OneToMany(mappedBy="utente",cascade=CascadeType.ALL) @Builder.Default private List<Abbonamento> abbonamenti=new ArrayList<>();
    @Override public Collection<? extends GrantedAuthority> getAuthorities(){return List.of(new SimpleGrantedAuthority("ROLE_"+ruolo.name()));}
    @Override public String getPassword(){return passwordHash;}
    @Override public String getUsername(){return email;}
    @Override public boolean isAccountNonExpired(){return true;}
    @Override public boolean isAccountNonLocked(){return true;}
    @Override public boolean isCredentialsNonExpired(){return true;}
    @Override public boolean isEnabled(){return true;}
    public enum Ruolo{TIFOSO,ADMIN}
}
