package br.com.orangeroute.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.orangeroute.model.Usuario;
import br.com.orangeroute.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    // LISTAR TODOS
    @GetMapping
    public List<Usuario> listar() {
        return repository.findAll();
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    // CRIAR
    @PostMapping
    public Usuario criar(@RequestBody Usuario usuario) {
        usuario.setIdUsuario(null);
        return repository.save(usuario);
    }

    // ATUALIZAR
    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        usuario.setIdUsuario(id);
        return repository.save(usuario);
    }

    // DELETAR
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}