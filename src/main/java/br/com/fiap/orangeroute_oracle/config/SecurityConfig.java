package br.com.fiap.orangeroute_oracle.config;

import br.com.fiap.orangeroute_oracle.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // Habilita CORS 
            .cors(cors -> {})

            .csrf(csrf -> csrf.disable())

            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            //  Regras de jwt
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // ROTAS PÚBLICAS
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/usuario/**").permitAll()

                // Swagger
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()

                // ROTAS PROTEGIDAS

                .requestMatchers("/trilhas/**")
                    .hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.GET, "/comentarios/**")
                    .hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.POST, "/comentarios/**")
                    .hasAnyRole("USER", "ADMIN")

                .requestMatchers("/favoritos/**")
                    .hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.GET, "/links/**")
                    .hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/usuario/**")
                    .hasRole("ADMIN")

                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}