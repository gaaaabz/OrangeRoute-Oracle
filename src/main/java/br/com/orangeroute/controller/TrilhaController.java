package br.com.orangeroute.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.orangeroute.model.TrilhaCarreira;
import br.com.orangeroute.repository.TrilhaRepository;

@RestController
@RequestMapping("/trilhas")
public class TrilhaController {

    @Autowired
    private TrilhaRepository repository;

    @GetMapping
    public List<TrilhaCarreira> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public TrilhaCarreira buscar(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public TrilhaCarreira criar(@RequestBody TrilhaCarreira trilha) {
        trilha.setIdTrilhaCarreira(null);
        return repository.save(trilha);
    }

    @PutMapping("/{id}")
    public TrilhaCarreira atualizar(@PathVariable Long id, @RequestBody TrilhaCarreira trilha) {
        trilha.setIdTrilhaCarreira(id);
        return repository.save(trilha);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}