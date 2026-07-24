package it.unife.ticketstadio.config;

import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Configurazione CORS: definisce quali origini (siti web) possono chiamare le nostre API.
 * Serve perché il frontend gira su un dominio/porta diversi dal backend, e senza queste
 * regole il browser bloccherebbe le richieste per motivi di sicurezza.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration c = new CorsConfiguration();

        // Consento le richieste dal frontend in sviluppo (localhost su qualsiasi porta).
        c.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
        // Metodi HTTP ammessi.
        c.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Tutti gli header sono ammessi nelle richieste.
        c.setAllowedHeaders(List.of("*"));
        // Header "Authorization" reso leggibile al frontend (contiene il token).
        c.setExposedHeaders(List.of("Authorization"));
        // Permette l'invio di credenziali/cookie.
        c.setAllowCredentials(true);
        // Il browser può mettere in cache la risposta preflight per 1 ora.
        c.setMaxAge(3600L);

        // Applico questa configurazione a tutti gli endpoint sotto "/api/**".
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/api/**", c);
        return new CorsFilter(src);
    }
}
