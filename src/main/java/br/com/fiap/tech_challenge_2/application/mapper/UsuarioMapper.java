package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "login", source = "login")
    @Mapping(target = "endereco", source = "endereco")
    @Mapping(target = "tipoUsuario", source = "tipoUsuario")
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

    // Mapeamento de TipoUsuarioDTO para TipoUsuario
    default TipoUsuario mapTipoUsuario(br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO tipoUsuarioDTO) {
        if (tipoUsuarioDTO == null) {
            return null;
        }
        return new TipoUsuario(tipoUsuarioDTO.id(), tipoUsuarioDTO.nome());
    }

    // Mapeamento de TipoUsuario para TipoUsuarioDTO
    default br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO mapTipoUsuarioDTO(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return null;
        }
        return new br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO(tipoUsuario.getId(), tipoUsuario.getNome());
    }
}