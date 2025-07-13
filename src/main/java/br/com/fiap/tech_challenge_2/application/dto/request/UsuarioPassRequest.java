package br.com.fiap.tech_challenge_2.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UsuarioPassRequest(
        @NotBlank(message = "Login é obrigatório")
        String login,

        @NotBlank(message = "Senha Atual é obrigatória")
        String senhaOld,

        @NotBlank(message = "Senha Nova é obrigatória")
        String senhaNew,

        @NotBlank(message = "Senha Nova é obrigatória")
        String senhaConfirm
) {}