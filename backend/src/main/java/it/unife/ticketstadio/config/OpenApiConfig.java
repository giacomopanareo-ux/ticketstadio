package it.unife.ticketstadio.config;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configurazione della documentazione OpenAPI/Swagger.
 *
 * - @OpenAPIDefinition -> definisce titolo, versione e descrizione dell'API.
 * - @SecurityScheme    -> dichiara lo schema di autenticazione "bearerAuth" (token JWT),
 *   così Swagger UI mostra il pulsante per inserire il token e provare gli endpoint protetti.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "TicketStadio API",
                version = "1.0",
                description = "API REST vendita biglietti calcio"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
public class OpenApiConfig {
}
