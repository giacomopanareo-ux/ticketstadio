package it.unife.ticketstadio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale dell'applicazione Spring Boot.
 * Il metodo main è il punto di ingresso: avvia l'intero backend.
 *
 * @SpringBootApplication attiva la configurazione automatica, la scansione dei componenti
 * (controller, service, repository, ecc.) e la configurazione di Spring Boot.
 */
@SpringBootApplication
public class TicketstadioApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketstadioApplication.class, args);
    }
}
