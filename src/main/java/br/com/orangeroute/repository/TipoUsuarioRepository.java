package br.com.orangeroute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.orangeroute.model.TipoUsuario;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {
}