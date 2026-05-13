package com.projetopessoal.projeto.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeEmailRequest {
    @NotBlank(message = "Senha atual é obrigatória")
    private String currentPassword;

    @NotBlank(message = "Novo e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String newEmail;
}
