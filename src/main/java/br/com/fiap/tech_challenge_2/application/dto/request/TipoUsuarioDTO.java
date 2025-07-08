package br.com.fiap.tech_challenge_2.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TipoUsuarioDTO(

        Long id,
        @NotBlank(message = "O nome do tipo de usuário é obrigatório")
        String nome
) {}