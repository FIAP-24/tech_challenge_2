package br.com.fiap.tech_challenge_2.application.dto.response;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record UsuarioResponse(
        Long id,
        String nome,
        String perfil,
        String email,
        String login,
        EnderecoDTO endereco,
        @JsonProperty("data_atualizacao")
        LocalDate dataUpdate
) {}