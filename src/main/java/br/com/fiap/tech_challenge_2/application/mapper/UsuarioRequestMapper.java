package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {EnderecoMapper.class})
public interface UsuarioRequestMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataUpdate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "senha", ignore = true)
    @Mapping(source = "endereco", target = "endereco")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "login", target = "login")
    @Mapping(source = "perfil", target = "perfil")
    Usuario toEntity(UsuarioRequest request);

    default Usuario toEntity(UsuarioRequest request, String hashedPassword) {
        Usuario usuario = toEntity(request);
        usuario.setSenha(hashedPassword);
        return usuario;
    }

}
