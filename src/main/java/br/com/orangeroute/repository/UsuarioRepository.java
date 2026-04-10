package br.com.orangeroute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.orangeroute.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}