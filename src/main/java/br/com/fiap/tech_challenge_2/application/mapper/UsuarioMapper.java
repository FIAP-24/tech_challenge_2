package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.infrastructure.Entity.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EnderecoMapper.class, TipoUsuarioMapper.class})
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Mapping(source = "endereco", target = "endereco")
    @Mapping(source = "tipoUsuario", target = "tipoUsuario")
    Usuario toModel(UsuarioEntity entity);

    @Mapping(source = "endereco", target = "endereco")
    @Mapping(source = "tipoUsuario", target = "tipoUsuario")
    UsuarioEntity toEntity(Usuario model);
}
