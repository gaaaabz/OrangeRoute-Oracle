package br.com.orangeroute.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.orangeroute.model.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByUsuario_IdUsuario(Long id);
}