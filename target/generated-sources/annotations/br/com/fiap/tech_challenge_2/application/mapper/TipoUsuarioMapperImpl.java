package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-08T20:32:45-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class TipoUsuarioMapperImpl implements TipoUsuarioMapper {

    @Override
    public TipoUsuarioDTO toDTO(TipoUsuario tipoUsuario) {
        if ( tipoUsuario == null ) {
            return null;
        }

        Long id = null;
        String nome = null;

        id = tipoUsuario.getId();
        nome = tipoUsuario.getNome();

        TipoUsuarioDTO tipoUsuarioDTO = new TipoUsuarioDTO( id, nome );

        return tipoUsuarioDTO;
    }

    @Override
    public TipoUsuario toEntity(TipoUsuarioDTO dto) {
        if ( dto == null ) {
            return null;
        }

        TipoUsuario tipoUsuario = new TipoUsuario();

        tipoUsuario.setId( dto.id() );
        tipoUsuario.setNome( dto.nome() );

        return tipoUsuario;
    }
}
