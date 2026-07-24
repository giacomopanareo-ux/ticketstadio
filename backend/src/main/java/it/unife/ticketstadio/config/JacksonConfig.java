package it.unife.ticketstadio.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.*;

/**
 * Configurazione di Jackson (la libreria che converte gli oggetti Java in JSON).
 * Registra il modulo per Hibernate 6, così le entità con relazioni "lazy" vengono
 * serializzate correttamente invece di generare errori.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Module hibernate6Module() {
        Hibernate6Module m = new Hibernate6Module();
        // Forza il caricamento delle relazioni lazy durante la serializzazione JSON.
        m.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, true);
        return m;
    }
}
