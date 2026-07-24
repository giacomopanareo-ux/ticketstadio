package it.unife.ticketstadio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

/**
 * Entity che rappresenta un utente sulla tabella "utente".
 *
 * Implementa UserDetails di Spring Security: questo permette a Spring di usare
 * direttamente questa entità per gestire login e autorizzazioni. I metodi
 * getUsername()/getPassword()/getAuthorities() sono richiesti da quell'interfaccia.
 */
@Entity
@Table(name = "utente", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utente implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    /** Email: identifica univocamente l'utente ed è usata come "username" per il login. */
    @Column(nullable = false, unique = true)
    private String email;

    /** Hash della password (mai la password in chiaro). @JsonIgnore -> non esposto nelle risposte JSON. */
    @JsonIgnore
    @Column(nullable = false)
    private String passwordHash;

    /** Ruolo dell'utente; di default un nuovo iscritto è un TIFOSO. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Ruolo ruolo = Ruolo.TIFOSO;

    @Column(nullable = false)
    private LocalDate dataRegistrazione;

    /** Biglietti e abbonamenti dell'utente (non serializzati nel JSON). */
    @JsonIgnore
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Biglietto> biglietti = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Abbonamento> abbonamenti = new ArrayList<>();

    // --- Metodi richiesti dall'interfaccia UserDetails di Spring Security ---

    /** Ruolo dell'utente convertito nel formato atteso da Spring (prefisso "ROLE_"). */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + ruolo.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // Gli account non scadono e non vengono bloccati: restituiscono sempre true.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /** Ruoli possibili di un utente. */
    public enum Ruolo { TIFOSO, ADMIN }
}
