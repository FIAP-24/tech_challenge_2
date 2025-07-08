package br.com.fiap.tech_challenge_2.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoDTO(
        @NotBlank(message = "O logradouro é obrigatório")
        @Size(max = 100, message = "O logradouro deve ter no máximo 100 caracteres")
        @Schema(description = "Nome da rua/avenida/logradouro", example = "Avenida Paulista")
        String logradouro,

        @NotBlank(message = "O número é obrigatório")
        @Size(max = 10, message = "O número deve ter no máximo 10 caracteres")
        @Schema(description = "Número do endereço", example = "1234")
        String numero,

        @Size(max = 50, message = "O complemento deve ter no máximo 50 caracteres")
        @Schema(description = "Complemento do endereço", example = "Apto 101", nullable = true)
        String complemento,

        @NotBlank(message = "O bairro é obrigatório")
        @Size(max = 50, message = "O bairro deve ter no máximo 50 caracteres")
        @Schema(description = "Bairro do endereço", example = "Bela Vista")
        String bairro,

        @NotBlank(message = "A cidade é obrigatória")
        @Size(max = 50, message = "A cidade deve ter no máximo 50 caracteres")
        @Schema(description = "Cidade do endereço", example = "São Paulo")
        String cidade,

        @NotBlank(message = "O estado é obrigatório")
        @Size(min = 2, max = 2, message = "O estado deve ter exatamente 2 caracteres")
        @Schema(description = "Sigla do estado (UF)", example = "SP")
        String estado,

        @NotBlank(message = "O CEP é obrigatório")
        @Size(min = 8, max = 8, message = "O CEP deve ter exatamente 8 dígitos")
        @Pattern(regexp = "\\d{8}", message = "O CEP deve conter apenas números")
        @Schema(description = "CEP do endereço (sem hífen)", example = "01310930")
        String cep
) {}