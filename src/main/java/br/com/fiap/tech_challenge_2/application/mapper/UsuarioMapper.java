package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "login", source = "login")
    @Mapping(target = "endereco", source = "endereco")
    @Mapping(target = "dataUpdate", source = "dataUpdate")
    UsuarioResponse toResponse(Usuario usuario);

    Set<UsuarioResponse> toResponseSet(Set<Usuario> usuarios);

    List<UsuarioResponse> toResponseList(List<Usuario> usuarios);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataUpdate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "senha", ignore = true)
    Usuario toEntity(UsuarioRequest request);


    @Mapping(target = "dataUpdate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "senha", ignore = true)
    Usuario toEntity(UsuarioRequest request, Long id);

    default Usuario toEntity(UsuarioRequest request, Long id, String hashedPassword) {
        Usuario usuario = toEntity(request);
        usuario.setId(id);
        usuario.setSenha(hashedPassword);
        return usuario;
    }
}