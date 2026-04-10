package br.com.orangeroute.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.orangeroute.model.Comentario;
import br.com.orangeroute.repository.ComentarioRepository;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioRepository repository;

    @GetMapping
    public List<Comentario> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Comentario buscar(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public Comentario criar(@RequestBody Comentario comentario) {
        comentario.setIdComentario(null);
        return repository.save(comentario);
    }

    @PutMapping("/{id}")
    public Comentario atualizar(@PathVariable Long id, @RequestBody Comentario comentario) {
        comentario.setIdComentario(id);
        return repository.save(comentario);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}