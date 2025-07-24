package br.com.fiap.tech_challenge_2.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioEditPassRequest(
        @NotBlank(message = "Login é obrigatório")
        String login,

        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        @NotBlank(message = "Senha Atual é obrigatória")
        String senhaOld,

        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        @NotBlank(message = "Senha Nova é obrigatória")
        String senhaNew,

        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        @NotBlank(message = "Senha Confirmação é obrigatória")
        String senhaConfirm
) {}