package br.com.orangeroute.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.orangeroute.model.TrilhaCarreira;

public interface TrilhaRepository extends JpaRepository<TrilhaCarreira, Long> {
}