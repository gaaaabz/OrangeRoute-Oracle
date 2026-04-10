package br.com.orangeroute.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import br.com.orangeroute.model.TipoUsuario;

import lombok.Data;

@Data
@Entity
@Table(name = "T_OR_USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @NotBlank
    private String nmUsuario;

    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    private String atUsuario;

    @ManyToOne
    @JoinColumn(name = "id_tipo_usuario")
    private TipoUsuario tipoUsuario;
}