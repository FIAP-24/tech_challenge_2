package br.com.fiap.tech_challenge_2.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ItemCardapioResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        @JsonProperty("disponivel_apenas_no_local")
        boolean disponivelApenasNoLocal,
        @JsonProperty("foto_path")
        String fotoPath,
        @JsonProperty("preco_formatado")
        String precoFormatado
) {} 