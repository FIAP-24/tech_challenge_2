package br.com.fiap.tech_challenge_2.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemCardapioRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        String descricao,

        @NotNull(message = "Preço é obrigatório")
        @Positive(message = "Preço deve ser maior que zero")
        Double preco,

        boolean disponivelApenasNoLocal,

        String fotoPath,

        @NotNull(message = "ID do restaurante é obrigatório")
        Long restauranteId
) {} 