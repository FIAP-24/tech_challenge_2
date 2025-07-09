package br.com.fiap.tech_challenge_2.mapper;

import br.com.fiap.tech_challenge_2.application.mapper.EnderecoMapper;
import br.com.fiap.tech_challenge_2.application.mapper.UsuarioMapper;
import br.com.fiap.tech_challenge_2.domain.enums.Perfil;
import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsuarioEntityMapperTest {

    private final UsuarioMapper mapper = UsuarioMapper.INSTANCE;
    private final EnderecoMapper enderecoMapper = EnderecoMapper.INSTANCE;

    private Endereco createTestEndereco() {
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setComplemento("Apto 101");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("12345678");
        return endereco;
    }

    @Test
    void toResponse_deveMapearUsuarioParaUsuarioResponse() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joao");
        usuario.setEndereco(createTestEndereco());
        usuario.setDataUpdate(LocalDate.now());

        UsuarioResponse response = mapper.toResponse(usuario);

        assertEquals(usuario.getId(), response.id());
        assertEquals(usuario.getNome(), response.nome());
        assertEquals(usuario.getEmail(), response.email());
        assertEquals(usuario.getLogin(), response.login());
        assertEquals(response.endereco().logradouro(), usuario.getEndereco().getLogradouro());
        assertEquals(response.endereco().numero(), usuario.getEndereco().getNumero());
        assertEquals(response.endereco().complemento(), usuario.getEndereco().getComplemento());
        assertEquals(response.endereco().bairro(), usuario.getEndereco().getBairro());
        assertEquals(response.endereco().cidade(), usuario.getEndereco().getCidade());
        assertEquals(response.endereco().estado(), usuario.getEndereco().getEstado());
        assertEquals(response.endereco().cep(), usuario.getEndereco().getCep());
        assertEquals(usuario.getDataUpdate(), response.dataUpdate());
    }

    @Test
    void toResponseSet_deveMapearSetDeUsuariosParaSetDeUsuarioResponse() {
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setNome("Maria");
        usuario.setEmail("maria@email.com");
        usuario.setLogin("maria");
        usuario.setEndereco(createTestEndereco());
        usuario.setDataUpdate(LocalDate.now());

        Set<Usuario> usuarios = new HashSet<>();
        usuarios.add(usuario);

        Set<UsuarioResponse> responses = mapper.toResponseSet(usuarios);

        assertEquals(1, responses.size());
        UsuarioResponse response = responses.iterator().next();
        assertEquals(usuario.getNome(), response.nome());
    }

    @Test
    void toResponseList_deveMapearListDeUsuariosParaListDeUsuarioResponse() {
        Usuario usuario = new Usuario();
        usuario.setId(3L);
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@email.com");
        usuario.setLogin("carlos");
        usuario.setEndereco(createTestEndereco());
        usuario.setDataUpdate(LocalDate.now());

        List<Usuario> usuarios = List.of(usuario);

        List<UsuarioResponse> responses = mapper.toResponseList(usuarios);

        assertEquals(1, responses.size());
        assertEquals(usuario.getNome(), responses.get(0).nome());
    }

    @Test
    void toEntity_deveMapearUsuarioRequestParaUsuario() {
        EnderecoDTO enderecoDTO = enderecoMapper.toEnderecoDTO(createTestEndereco());
        UsuarioRequest request = new UsuarioRequest("Ana", "ana@email.com", Perfil.CLIENTE,"ana", "Rua 4", enderecoDTO);

        Usuario usuario = mapper.toEntity(request);

        assertNull(usuario.getId());
        assertEquals(request.nome(), usuario.getNome());
        assertEquals(request.email(), usuario.getEmail());
        assertEquals(request.login(), usuario.getLogin());
        assertEquals(request.endereco().logradouro(), usuario.getEndereco().getLogradouro());
        assertEquals(request.endereco().numero(), usuario.getEndereco().getNumero());
        assertEquals(request.endereco().complemento(), usuario.getEndereco().getComplemento());
        assertEquals(request.endereco().bairro(), usuario.getEndereco().getBairro());
        assertEquals(request.endereco().cidade(), usuario.getEndereco().getCidade());
        assertEquals(request.endereco().estado(), usuario.getEndereco().getEstado());
        assertEquals(request.endereco().cep(), usuario.getEndereco().getCep());
        assertNotNull(usuario.getDataUpdate());
        assertNull(usuario.getSenha());
    }

    @Test
    void toEntityComId_deveMapearUsuarioRequestParaUsuarioComId() {
        EnderecoDTO enderecoDTO = enderecoMapper.toEnderecoDTO(createTestEndereco());
        UsuarioRequest request = new UsuarioRequest("Ana", "ana@email.com", Perfil.CLIENTE,"ana", "Rua 4", enderecoDTO);
        Usuario usuario = mapper.toEntity(request, 10L);

        assertEquals(10L, usuario.getId());
        assertEquals(request.nome(), usuario.getNome());
        assertEquals(request.email(), usuario.getEmail());
        assertEquals(request.login(), usuario.getLogin());
        assertEquals(request.endereco().logradouro(), usuario.getEndereco().getLogradouro());
        assertEquals(request.endereco().numero(), usuario.getEndereco().getNumero());
        assertEquals(request.endereco().complemento(), usuario.getEndereco().getComplemento());
        assertEquals(request.endereco().bairro(), usuario.getEndereco().getBairro());
        assertEquals(request.endereco().cidade(), usuario.getEndereco().getCidade());
        assertEquals(request.endereco().estado(), usuario.getEndereco().getEstado());
        assertEquals(request.endereco().cep(), usuario.getEndereco().getCep());
        assertNotNull(usuario.getDataUpdate());
        assertNull(usuario.getSenha());
    }

    @Test
    void toEntityComIdESenha_deveMapearUsuarioRequestParaUsuarioComIdESenha() {
        EnderecoDTO enderecoDTO = enderecoMapper.toEnderecoDTO(createTestEndereco());
        UsuarioRequest request = new UsuarioRequest("Ana", "ana@email.com", Perfil.CLIENTE,"ana", "Rua 4", enderecoDTO);
        String senhaHash = "senha123";
        Usuario usuario = mapper.toEntity(request, 20L, senhaHash);

        assertEquals(20L, usuario.getId());
        assertEquals(request.nome(), usuario.getNome());
        assertEquals(request.email(), usuario.getEmail());
        assertEquals(request.login(), usuario.getLogin());
        assertEquals(request.endereco().logradouro(), usuario.getEndereco().getLogradouro());
        assertEquals(request.endereco().numero(), usuario.getEndereco().getNumero());
        assertEquals(request.endereco().complemento(), usuario.getEndereco().getComplemento());
        assertEquals(request.endereco().bairro(), usuario.getEndereco().getBairro());
        assertEquals(request.endereco().cidade(), usuario.getEndereco().getCidade());
        assertEquals(request.endereco().estado(), usuario.getEndereco().getEstado());
        assertEquals(request.endereco().cep(), usuario.getEndereco().getCep());
        assertEquals(senhaHash, usuario.getSenha());
        assertNotNull(usuario.getDataUpdate());
    }
}