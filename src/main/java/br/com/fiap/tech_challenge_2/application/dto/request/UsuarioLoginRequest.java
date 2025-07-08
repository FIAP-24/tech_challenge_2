package br.com.fiap.tech_challenge_2.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UsuarioLoginRequest(
        @NotBlank(message = "Login é obrigatório")
        String login,

        @NotBlank(message = "Senha é obrigatória")
        String senha
) {}