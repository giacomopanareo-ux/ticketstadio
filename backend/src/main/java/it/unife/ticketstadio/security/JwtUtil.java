package it.unife.ticketstadio.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Classe di utilità per la gestione dei token JWT: creazione, lettura e validazione.
 */
@Component
public class JwtUtil {

    // Valori letti dal file di configurazione (application.properties/yml).
    @Value("${jwt.secret}")
    private String secret;       // chiave segreta (in Base64) usata per firmare i token

    @Value("${jwt.expiration}")
    private long expiration;     // durata del token in millisecondi

    /** Ricostruisce la chiave di firma a partire dal segreto configurato. */
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /**
     * Genera un token JWT per l'utente dato.
     * Il token contiene: subject (email), ruoli, data di emissione e di scadenza, ed è firmato.
     */
    public String generateToken(UserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey())
                .compact();
    }

    /** Estrae lo username (email) contenuto nel token. */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Verifica che il token sia valido per l'utente:
     * lo username deve coincidere E il token non deve essere scaduto.
     */
    public boolean isValid(String token, UserDetails user) {
        return extractUsername(token).equals(user.getUsername())
                && !getClaims(token).getExpiration().before(new Date());
    }

    /** Legge il contenuto (claims) del token, verificandone contestualmente la firma. */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
