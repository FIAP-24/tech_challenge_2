package br.com.fiap.tech_challenge_2.application.dto.response;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;

import java.util.List;

public record RestauranteResponse(
        Long id,
        String nome,
        EnderecoDTO endereco,
        String tipoCozinha,
        String horarioFuncionamento,
        UsuarioResponse dono,
        List<ItemCardapioResponse> cardapio
) {}
