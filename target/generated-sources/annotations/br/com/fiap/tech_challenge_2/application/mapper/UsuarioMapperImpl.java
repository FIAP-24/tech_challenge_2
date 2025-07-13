package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.TipoUsuario;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-13T19:57:12-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioResponse toResponse(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        String email = null;
        String login = null;
        EnderecoDTO endereco = null;
        LocalDate dataUpdate = null;
        String perfil = null;

        id = usuario.getId();
        nome = usuario.getNome();
        email = usuario.getEmail();
        login = usuario.getLogin();
        endereco = enderecoToEnderecoDTO( usuario.getEndereco() );
        dataUpdate = usuario.getDataUpdate();
        perfil = usuarioTipoUsuarioNome( usuario );

        UsuarioResponse usuarioResponse = new UsuarioResponse( id, nome, perfil, email, login, endereco, dataUpdate );

        return usuarioResponse;
    }

    @Override
    public Set<UsuarioResponse> toResponseSet(Set<Usuario> usuarios) {
        if ( usuarios == null ) {
            return null;
        }

        Set<UsuarioResponse> set = new LinkedHashSet<UsuarioResponse>( Math.max( (int) ( usuarios.size() / .75f ) + 1, 16 ) );
        for ( Usuario usuario : usuarios ) {
            set.add( toResponse( usuario ) );
        }

        return set;
    }

    @Override
    public List<UsuarioResponse> toResponseList(List<Usuario> usuarios) {
        if ( usuarios == null ) {
            return null;
        }

        List<UsuarioResponse> list = new ArrayList<UsuarioResponse>( usuarios.size() );
        for ( Usuario usuario : usuarios ) {
            list.add( toResponse( usuario ) );
        }

        return list;
    }

    @Override
    public Usuario toEntity(UsuarioRequest request) {
        if ( request == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setNome( request.nome() );
        usuario.setEmail( request.email() );
        usuario.setLogin( request.login() );
        usuario.setEndereco( enderecoDTOToEndereco( request.endereco() ) );

        usuario.setDataUpdate( java.time.LocalDate.now() );

        return usuario;
    }

    @Override
    public Usuario toEntity(UsuarioRequest request, Long id) {
        if ( request == null && id == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        if ( request != null ) {
            usuario.setNome( request.nome() );
            usuario.setEmail( request.email() );
            usuario.setLogin( request.login() );
            usuario.setEndereco( enderecoDTOToEndereco( request.endereco() ) );
        }
        usuario.setId( id );
        usuario.setDataUpdate( java.time.LocalDate.now() );

        return usuario;
    }

    protected EnderecoDTO enderecoToEnderecoDTO(Endereco endereco) {
        if ( endereco == null ) {
            return null;
        }

        String logradouro = null;
        String numero = null;
        String complemento = null;
        String bairro = null;
        String cidade = null;
        String estado = null;
        String cep = null;

        logradouro = endereco.getLogradouro();
        numero = endereco.getNumero();
        complemento = endereco.getComplemento();
        bairro = endereco.getBairro();
        cidade = endereco.getCidade();
        estado = endereco.getEstado();
        cep = endereco.getCep();

        EnderecoDTO enderecoDTO = new EnderecoDTO( logradouro, numero, complemento, bairro, cidade, estado, cep );

        return enderecoDTO;
    }

    private String usuarioTipoUsuarioNome(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }
        TipoUsuario tipoUsuario = usuario.getTipoUsuario();
        if ( tipoUsuario == null ) {
            return null;
        }
        String nome = tipoUsuario.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }

    protected Endereco enderecoDTOToEndereco(EnderecoDTO enderecoDTO) {
        if ( enderecoDTO == null ) {
            return null;
        }

        Endereco endereco = new Endereco();

        endereco.setLogradouro( enderecoDTO.logradouro() );
        endereco.setNumero( enderecoDTO.numero() );
        endereco.setComplemento( enderecoDTO.complemento() );
        endereco.setBairro( enderecoDTO.bairro() );
        endereco.setCidade( enderecoDTO.cidade() );
        endereco.setEstado( enderecoDTO.estado() );
        endereco.setCep( enderecoDTO.cep() );

        return endereco;
    }
}
