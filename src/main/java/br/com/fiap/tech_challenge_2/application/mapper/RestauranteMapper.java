package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteDTO;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestauranteMapper {
    RestauranteDTO toDTO(Restaurante restaurante);

    Restaurante toEntity(RestauranteDTO dto);
}
