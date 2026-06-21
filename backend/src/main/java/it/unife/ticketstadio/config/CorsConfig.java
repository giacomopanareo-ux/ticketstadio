package it.unife.ticketstadio.config;
import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;
import org.springframework.web.filter.CorsFilter;
import java.util.List;
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration c=new CorsConfiguration();
        c.setAllowedOriginPatterns(List.of("http://localhost:*","http://127.0.0.1:*"));
        c.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        c.setAllowedHeaders(List.of("*"));
        c.setExposedHeaders(List.of("Authorization"));
        c.setAllowCredentials(true);
        c.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource src=new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/api/**",c);
        return new CorsFilter(src);
    }
}
