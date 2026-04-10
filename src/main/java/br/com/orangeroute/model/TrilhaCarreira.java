package br.com.orangeroute.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "T_OR_TRILHA_CARREIRA")
public class TrilhaCarreira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTrilhaCarreira;

    @NotBlank
    private String ttTrilhaCarreira;

    private String cdTrilhaCarreira;
}