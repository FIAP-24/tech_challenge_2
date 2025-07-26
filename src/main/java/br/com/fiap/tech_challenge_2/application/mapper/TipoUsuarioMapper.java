package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TipoUsuarioMapper {

    TipoUsuarioDTO toDTO(TipoUsuario tipoUsuario);

    TipoUsuario toEntity(TipoUsuarioDTO dto);
}