package com.projetopessoal.projeto.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContaRequest {
    @NotBlank(message = "Nome da conta é obrigatório")
    private String nome;

    @NotBlank(message = "Nome do banco é obrigatório")
    private String banco;

    private String tipo;
    private String cor;
    private String icone;
}
