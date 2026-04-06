package br.com.fiap.orangeroute_oracle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.orangeroute_oracle.entity.*;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    
    Optional<Usuario> findByEmail(String email);
}
