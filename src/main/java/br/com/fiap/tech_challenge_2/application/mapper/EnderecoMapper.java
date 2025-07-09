package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.infrastructure.Entity.EnderecoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    EnderecoMapper INSTANCE = Mappers.getMapper(EnderecoMapper.class);

    Endereco toModel(EnderecoEntity entity);

    EnderecoEntity toEntity(Endereco model);
}
