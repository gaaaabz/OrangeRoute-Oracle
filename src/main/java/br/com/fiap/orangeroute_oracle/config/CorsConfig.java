package br.com.fiap.orangeroute_oracle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // 🔥 ACEITA QUALQUER ORIGEM (CORRETO COM CREDENCIAIS)
        config.setAllowedOriginPatterns(List.of("*"));

        // 🔓 Métodos
        config.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        // 🔓 Headers
        config.setAllowedHeaders(List.of("*"));

        // 🔓 Token (Authorization)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}