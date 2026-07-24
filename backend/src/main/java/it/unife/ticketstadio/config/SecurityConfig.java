package it.unife.ticketstadio.config;

import it.unife.ticketstadio.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configurazione centrale di Spring Security.
 *
 * - @EnableWebSecurity   -> attiva la sicurezza web di Spring.
 * - @EnableMethodSecurity -> abilita le annotazioni tipo @PreAuthorize sui metodi/controller.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /**
     * Definisce la catena di filtri di sicurezza: quali richieste sono pubbliche,
     * quali richiedono autenticazione e come viene gestita la sessione.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF disabilitato: usiamo token JWT (API stateless), non sessioni con cookie.
                .csrf(AbstractHttpConfigurer::disable)
                // Nessuna sessione lato server: ogni richiesta si autentica col proprio token.
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Regole di autorizzazione, valutate dall'alto verso il basso.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()                       // login/registrazione: pubblici
                        .requestMatchers(HttpMethod.GET, "/api/partite/**").permitAll()    // lettura partite: pubblica
                        .requestMatchers(HttpMethod.GET, "/api/settori/**").permitAll()    // lettura settori: pubblica
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  // documentazione API: pubblica
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")                 // area admin: solo ADMIN
                        .requestMatchers("/api/**").authenticated()                        // tutto il resto delle API: autenticato
                        .anyRequest().permitAll())
                // Inserisco il filtro JWT prima del filtro standard di autenticazione username/password.
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /** Espone l'AuthenticationManager, usato dal service di login per verificare le credenziali. */
    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration c) throws Exception {
        return c.getAuthenticationManager();
    }

    /** Algoritmo usato per cifrare/verificare le password (BCrypt). */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
