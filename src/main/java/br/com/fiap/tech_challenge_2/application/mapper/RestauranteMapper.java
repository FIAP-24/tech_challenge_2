package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.RestauranteResponse;
import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RestauranteMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "endereco", source = "endereco")
    @Mapping(target = "tipoCozinha", source = "tipoCozinha")
    @Mapping(target = "horarioFuncionamento", source = "horarioFuncionamento")
    @Mapping(target = "dono", source = "dono")
    @Mapping(target = "cardapio", source = "cardapio")
    RestauranteResponse toResponse(Restaurante restaurante);

    Set<RestauranteResponse> toResponseSet(Set<Restaurante> restaurantes);
    List<RestauranteResponse> toResponseList(List<Restaurante> restaurantes);

    @Mapping(target = "id", ignore = true)
    Restaurante toEntity(RestauranteRequest dto);

    Restaurante toEntity(RestauranteRequest dto, Long id);
}
