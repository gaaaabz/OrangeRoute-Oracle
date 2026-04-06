package br.com.fiap.orangeroute_oracle.controller;

import br.com.fiap.orangeroute_oracle.dto.LoginDTO;
import br.com.fiap.orangeroute_oracle.dto.LoginResponseDTO;
import br.com.fiap.orangeroute_oracle.security.jwt.JwtService;
import br.com.fiap.orangeroute_oracle.security.service.CustomUserDetailsService;
import br.com.fiap.orangeroute_oracle.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO dto) {

        UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserByUsername(dto.getEmail());

        if (!new BCryptPasswordEncoder().matches(dto.getSenha(), user.getPassword())) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponseDTO(token, user.getUsuario()));
    }
}
