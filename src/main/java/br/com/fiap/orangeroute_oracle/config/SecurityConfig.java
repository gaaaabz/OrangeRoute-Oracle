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
            // 🔓 Habilita CORS (usa CorsConfig)
            .cors(cors -> {})

            // 🔒 Desabilita CSRF (API REST)
            .csrf(csrf -> csrf.disable())

            // 🔒 Sem sessão (JWT)
            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 🔐 Regras de autorização
            .authorizeHttpRequests(auth -> auth

                // 🔥 ESSENCIAL (preflight CORS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // =====================
                // 🔓 ROTAS PÚBLICAS
                // =====================
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/usuario/**").permitAll()

                // Swagger
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                ).permitAll()

                // =====================
                // 🔐 ROTAS PROTEGIDAS
                // =====================

                // TRILHAS
                .requestMatchers("/trilhas/**")
                    .hasAnyRole("USER", "ADMIN")

                // COMENTÁRIOS
                .requestMatchers(HttpMethod.GET, "/comentarios/**")
                    .hasAnyRole("USER", "ADMIN")

                // 🔥 AJUSTE AQUI (ANTES ERA hasRole("USER"))
                .requestMatchers(HttpMethod.POST, "/comentarios/**")
                    .hasAnyRole("USER", "ADMIN")

                // FAVORITOS
                // 🔥 AJUSTE AQUI TAMBÉM
                .requestMatchers("/favoritos/**")
                    .hasAnyRole("USER", "ADMIN")

                // LINKS
                .requestMatchers(HttpMethod.GET, "/links/**")
                    .hasAnyRole("USER", "ADMIN")

                // ADMIN
                .requestMatchers(HttpMethod.DELETE, "/usuario/**")
                    .hasRole("ADMIN")

                // QUALQUER OUTRA
                .anyRequest().authenticated()
            )

            // 🔐 Filtro JWT
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}