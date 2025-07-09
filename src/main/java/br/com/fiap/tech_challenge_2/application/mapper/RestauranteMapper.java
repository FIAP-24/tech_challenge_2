package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.infrastructure.Entity.RestauranteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EnderecoMapper.class, UsuarioMapper.class})
public interface RestauranteMapper {

    RestauranteMapper INSTANCE = Mappers.getMapper(RestauranteMapper.class);

    @Mapping(source = "enderecoEntity", target = "endereco")
    @Mapping(source = "dono", target = "dono")
    Restaurante toModel(RestauranteEntity entity);

    @Mapping(source = "endereco", target = "enderecoEntity")
    @Mapping(source = "dono", target = "dono")
    RestauranteEntity toEntity(Restaurante model);
}
