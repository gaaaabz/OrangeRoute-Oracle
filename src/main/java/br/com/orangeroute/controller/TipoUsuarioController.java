package br.com.orangeroute.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.orangeroute.model.TipoUsuario;
import br.com.orangeroute.repository.TipoUsuarioRepository;

@RestController
@RequestMapping("/tipos")
public class TipoUsuarioController {

    @Autowired
    private TipoUsuarioRepository repository;

    @GetMapping
    public List<TipoUsuario> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public TipoUsuario buscar(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PostMapping
    public TipoUsuario criar(@RequestBody TipoUsuario tipo) {
        tipo.setIdTipoUsuario(null);
        return repository.save(tipo);
    }

    @PutMapping("/{id}")
    public TipoUsuario atualizar(@PathVariable Long id, @RequestBody TipoUsuario tipo) {
        tipo.setIdTipoUsuario(id);
        return repository.save(tipo);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}