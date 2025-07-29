package br.com.fiap.tech_challenge_2.application.dto.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RestauranteDTO(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotNull(message = "Endereço é obrigatório")
        @Valid
        EnderecoDTO endereco,

        @NotBlank(message = "Tipo de cozinha é obrigatório")
        String tipoCozinha,

        @NotBlank(message = "Horário de funcionamento é obrigatório")
        String horarioFuncionamento,

        @NotNull(message = "ID do dono do restaurante é obrigatório")
        Long donoId
) {}