package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TipoUsuarioToDtoMapper {

    TipoUsuarioToDtoMapper INSTANCE = Mappers.getMapper(TipoUsuarioToDtoMapper.class);

    TipoUsuarioDTO toDTO(TipoUsuario tipoUsuario);

    TipoUsuario toEntity(TipoUsuarioDTO dto);
}