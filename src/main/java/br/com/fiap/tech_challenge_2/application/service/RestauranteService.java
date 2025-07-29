package br.com.fiap.tech_challenge_2.application.service;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteDTO;

import java.util.List;

public interface RestauranteService {
    RestauranteDTO create(RestauranteDTO restauranteDTO);
    RestauranteDTO findById(Long id);
    List<RestauranteDTO> findAll();
    RestauranteDTO update(Long id, RestauranteDTO restauranteDTO);
    void delete(Long id);
}