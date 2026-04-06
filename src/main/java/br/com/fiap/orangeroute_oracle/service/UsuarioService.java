package br.com.fiap.orangeroute_oracle.service;

import br.com.fiap.orangeroute_oracle.dto.UsuarioCreateDTO;
import br.com.fiap.orangeroute_oracle.dto.UsuarioResponseDTO;
import br.com.fiap.orangeroute_oracle.dto.UsuarioUpdateDTO;
import br.com.fiap.orangeroute_oracle.dto.UsuarioFotoDTO;
import br.com.fiap.orangeroute_oracle.entity.TipoUsuario;
import br.com.fiap.orangeroute_oracle.entity.Usuario;
import br.com.fiap.orangeroute_oracle.repository.TipoUsuarioRepository;
import br.com.fiap.orangeroute_oracle.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioResponseDTO cadastrarUsuario(UsuarioCreateDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Já existe um usuário cadastrado com este e-mail.");
        }

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(dto.getIdTipoUsuario())
                .orElseThrow(() -> new RuntimeException("Tipo de usuário inválido."));

        if (dto.getSenha() == null || dto.getSenha().length() < 8) {
            throw new RuntimeException("A senha deve ter pelo menos 8 caracteres.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNomeUsuario(dto.getNomeUsuario());
        novoUsuario.setEmail(dto.getEmail());

        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        novoUsuario.setTipoUsuario(tipoUsuario);
        novoUsuario.setAtivo("1");

        Usuario salvo = usuarioRepository.save(novoUsuario);
        return toResponseDTO(salvo);
    }

    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        return toResponseDTO(usuario);
    }

    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));

        if (dto.getNomeUsuario() != null && !dto.getNomeUsuario().isBlank())
            existente.setNomeUsuario(dto.getNomeUsuario());

        if (dto.getEmail() != null && !dto.getEmail().isBlank())
            existente.setEmail(dto.getEmail());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            if (dto.getSenha().length() < 8)
                throw new RuntimeException("A senha deve ter pelo menos 8 caracteres.");

            // 🔐 SENHA CRIPTOGRAFADA NO UPDATE
            existente.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        if (dto.getIdTipoUsuario() != null) {
            TipoUsuario tipo = tipoUsuarioRepository.findById(dto.getIdTipoUsuario())
                    .orElseThrow(() -> new RuntimeException("Tipo de usuário inválido."));
            existente.setTipoUsuario(tipo);
        }

        Usuario atualizado = usuarioRepository.save(existente);
        return toResponseDTO(atualizado);
    }

    public UsuarioResponseDTO atualizarFoto(Long id, UsuarioFotoDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        try {
            usuario.setFoto(dto.getFoto().getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar imagem do perfil.");
        }

        Usuario atualizado = usuarioRepository.save(usuario);
        return toResponseDTO(atualizado);
    }

    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado para exclusão.");
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        String fotoBase64 = null;

        if (usuario.getFoto() != null && usuario.getFoto().length > 0) {
            fotoBase64 = Base64.getEncoder().encodeToString(usuario.getFoto());
        }

        return new UsuarioResponseDTO(
                usuario.getIdUsuario(),
                usuario.getNomeUsuario(),
                usuario.getEmail(),
                usuario.getTipoUsuario().getIdTipoUsuario(),
                usuario.getTipoUsuario().getNomeTipoUsuario(),
                usuario.getAtivo(),
                fotoBase64
        );
    }
}