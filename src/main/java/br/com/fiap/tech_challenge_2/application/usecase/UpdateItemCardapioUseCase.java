package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.dto.request.ItemCardapioRequestDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.ItemCardapioResponse;

public interface UpdateItemCardapioUseCase {

    ItemCardapioResponse execute(Long id, ItemCardapioRequestDTO request);
} 