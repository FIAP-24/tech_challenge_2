package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-13T19:57:12-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class EnderecoMapperImpl implements EnderecoMapper {

    @Override
    public EnderecoDTO toEnderecoDTO(Endereco endereco) {
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

    @Override
    public Endereco toEndereco(EnderecoDTO enderecoDTO) {
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
