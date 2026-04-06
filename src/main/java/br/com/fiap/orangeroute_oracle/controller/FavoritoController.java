package br.com.fiap.orangeroute_oracle.controller;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.orangeroute_oracle.dto.FavoritoCreateDTO;
import br.com.fiap.orangeroute_oracle.dto.FavoritoResponseDTO;
import br.com.fiap.orangeroute_oracle.service.FavoritoService;

@RestController
@RequestMapping("/favoritos")
@CrossOrigin(origins = "*")
public class FavoritoController {

    @Autowired
    private FavoritoService service;

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        List<FavoritoResponseDTO> favoritos = service.listarTodos();

        List<Map<String, Object>> data = favoritos.stream().map(f -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("idFavorito", f.getIdFavorito());
            item.put("idUsuario", f.getIdUsuario());
            item.put("idTrilhaCarreira", f.getIdTrilhaCarreira());
            item.put("nomeUsuario", f.getNomeUsuario());
            item.put("tituloTrilha", f.getTituloTrilha());
            item.put("_links", Map.of(
                    "self", "/favoritos/" + f.getIdFavorito(),
                    "usuario", "/favoritos/usuario/" + f.getIdUsuario(),
                    "trilha", "/trilhas/" + f.getIdTrilhaCarreira()
            ));
            return item;
        }).collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("count", data.size());
        response.put("data", data);
        response.put("_links", Map.of(
                "self", "/favoritos",
                "create", "/favoritos (POST)"
        ));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable Long idUsuario) {
        List<FavoritoResponseDTO> favoritos = service.listarPorUsuario(idUsuario);

        List<Map<String, Object>> data = favoritos.stream().map(f -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("idFavorito", f.getIdFavorito());
            item.put("idUsuario", f.getIdUsuario());
            item.put("idTrilhaCarreira", f.getIdTrilhaCarreira());
            item.put("nomeUsuario", f.getNomeUsuario());
            item.put("tituloTrilha", f.getTituloTrilha());
            item.put("_links", Map.of(
                    "self", "/favoritos/" + f.getIdFavorito(),
                    "usuario", "/favoritos/usuario/" + f.getIdUsuario(),
                    "trilha", "/trilhas/" + f.getIdTrilhaCarreira()
            ));
            return item;
        }).collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("count", data.size());
        response.put("data", data);
        response.put("_links", Map.of(
                "self", "/favoritos/usuario/" + idUsuario,
                "all", "/favoritos",
                "create", "/favoritos (POST)"
        ));

        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody FavoritoCreateDTO dto) {
        FavoritoResponseDTO novo = service.criar(dto);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("idFavorito", novo.getIdFavorito());
        body.put("idUsuario", novo.getIdUsuario());
        body.put("idTrilhaCarreira", novo.getIdTrilhaCarreira());
        body.put("nomeUsuario", novo.getNomeUsuario());
        body.put("tituloTrilha", novo.getTituloTrilha());
        body.put("_links", Map.of(
                "self", "/favoritos/" + novo.getIdFavorito(),
                "usuario", "/favoritos/usuario/" + novo.getIdUsuario(),
                "trilha", "/trilhas/" + novo.getIdTrilhaCarreira(),
                "delete", "/favoritos/" + novo.getIdFavorito() + " (DELETE)"
        ));

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{idFavorito}")
    public ResponseEntity<?> remover(@PathVariable Long idFavorito) {
        service.remover(idFavorito);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Favorito removido com sucesso!");
        body.put("_links", Map.of(
                "all", "/favoritos",
                "create", "/favoritos (POST)"
        ));

        return ResponseEntity.ok(body);
    }
}
