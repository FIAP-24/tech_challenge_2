package br.com.fiap.tech_challenge_2.application.service;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.RestauranteResponse;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;

import java.util.List;
import java.util.Set;

public interface RestauranteService {

    Restaurante save(RestauranteRequest restauranteRequest);

    Restaurante findById(Long id);

    Set<Restaurante> findAll();

    Restaurante update(Long id, RestauranteRequest restauranteRequest);

    void delete(Long id);
}