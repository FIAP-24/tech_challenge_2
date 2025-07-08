package br.com.fiap.tech_challenge_2.application.service;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequestDTO;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;

import java.util.List;

public interface RestauranteService {
    Restaurante create(RestauranteRequestDTO restauranteDTO);
    Restaurante findById(Long id);
    List<Restaurante> findAll();
    Restaurante update(Long id, RestauranteRequestDTO restauranteDTO);
    void delete(Long id);
}