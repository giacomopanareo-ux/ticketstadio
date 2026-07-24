package it.unife.ticketstadio.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro che viene eseguito una volta per ogni richiesta HTTP (OncePerRequestFilter).
 * Il suo compito: leggere il token JWT dall'header "Authorization" e, se valido,
 * autenticare l'utente nel contesto di Spring Security.
 */
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        // 1) Leggo l'header Authorization. Se manca o non è un token "Bearer",
        //    lascio proseguire la richiesta senza autenticare (potrebbe essere un endpoint pubblico).
        String auth = req.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        // 2) Estraggo il token (togliendo il prefisso "Bearer ") e ricavo lo username (email).
        String token = auth.substring(7);
        String username = jwtUtil.extractUsername(token);

        // 3) Se ho uno username e non c'è già un utente autenticato, verifico il token.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isValid(token, user)) {
                // Token valido: creo l'oggetto di autenticazione e lo salvo nel contesto di sicurezza.
                var authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 4) Proseguo con la catena di filtri (la richiesta arriva al controller).
        chain.doFilter(req, res);
    }
}
