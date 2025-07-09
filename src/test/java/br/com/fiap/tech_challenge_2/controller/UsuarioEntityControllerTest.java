package br.com.fiap.tech_challenge_2.controller;

import br.com.fiap.tech_challenge_2.domain.enums.Perfil;
import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioLoginRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.interfaces.exception.AuthenticationException;
import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import br.com.fiap.tech_challenge_2.domain.model.Endereco;
import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import br.com.fiap.tech_challenge_2.application.service.UsuarioService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONCompareMode.LENIENT;

/**
 * @author Richard Silva on 23/05/25
 * @project tech_challenge_1
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioEntityControllerTest {

    @LocalServerPort
    private int port;

    @MockitoBean
    private UsuarioService usuarioService;

    private Usuario usuario;
    private Endereco endereco;

    @BeforeAll
    void setup() {
        RestAssured.port = port;

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("email@email.com");
        usuario.setNome("nome");
        usuario.setPerfil(Perfil.PROPRIETARIO.name());
        usuario.setLogin("login123");
        usuario.setSenha("senha");

        endereco = new Endereco();
        endereco.setBairro("bairro");
        endereco.setCep("01234567");
        endereco.setLogradouro("logradouro");
        endereco.setNumero("123");
        endereco.setComplemento("complemento");
        endereco.setCidade("cidade");
        endereco.setEstado("estado");

        usuario.setEndereco(endereco);

    }


    @Test
    void shouldReturnUsuariosComSucesso() throws JSONException {
        EnderecoDTO enderecoDTO = new EnderecoDTO(endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(), endereco.getBairro(), endereco.getCidade(), endereco.getEstado(), endereco.getCep());
        UsuarioResponse usuarioResponse = new UsuarioResponse(usuario.getId(), usuario.getNome(), Perfil.PROPRIETARIO, usuario.getEmail(), usuario.getLogin(), enderecoDTO, LocalDate.now());

        Set<UsuarioResponse> lista = new HashSet<>();
        lista.add(usuarioResponse);

        when(usuarioService.findAll()).thenReturn(lista);

        Response response = given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v1/usuarios")
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();

        String expectedResponse = getContentFromFile("response/lista-usuario-response-successfully.json");

        CustomComparator customComparator = buildCustomComparatorIgnoreFields(Arrays.asList("timestamp", "id", "data_atualizacao"));
        JSONAssert.assertEquals(expectedResponse, response.asPrettyString(), customComparator);
    }

    @Test
    void shouldFindUsuarioByIDComSucesso() throws JSONException {
        EnderecoDTO enderecoDTO = new EnderecoDTO(endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(), endereco.getBairro(), endereco.getCidade(), endereco.getEstado(), endereco.getCep());
        UsuarioResponse usuarioResponse = new UsuarioResponse(usuario.getId(), usuario.getNome(), Perfil.PROPRIETARIO, usuario.getEmail(), usuario.getLogin(), enderecoDTO, LocalDate.now());

        when(usuarioService.findById(1L)).thenReturn(usuarioResponse);

        Response response = given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v1/usuarios/1")
                .then()
                .statusCode(200)
                .log().body()
                .extract().response();

        String expectedResponse = getContentFromFile("response/usuario-response-successfully.json");

        CustomComparator customComparator = buildCustomComparatorIgnoreFields(Arrays.asList("timestamp", "id", "data.data_atualizacao"));
        JSONAssert.assertEquals(expectedResponse, response.asPrettyString(), customComparator);
    }

    @Test
    void shouldSalvarUsuarioComSucesso() throws JSONException {
        EnderecoDTO enderecoDTO = new EnderecoDTO(
                "logradouro", "123", "complemento",
                "bairro", "cidade", "estado", "01234567"
        );
        UsuarioRequest usuarioRequest = new UsuarioRequest("nome", "email@email.com", Perfil.PROPRIETARIO, "login123", "senha123", enderecoDTO);


        UsuarioResponse usuarioResponse = new UsuarioResponse(
                1L, "nome", Perfil.PROPRIETARIO, "email@email.com",
                "login123", enderecoDTO, LocalDate.now()
        );

        when(usuarioService.save(any(UsuarioRequest.class))).thenReturn(usuarioResponse);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(usuarioRequest)
                .when()
                .post("/api/v1/usuarios")
                .then()
                .statusCode(201)
                .log().body()
                .extract().response();

        String expectedResponse = getContentFromFile("response/cadastro-usuario-response-successfully.json");

        CustomComparator customComparator = buildCustomComparatorIgnoreFields(Arrays.asList("timestamp", "id", "data.data_atualizacao"));
        JSONAssert.assertEquals(expectedResponse, response.asPrettyString(), customComparator);
    }

    @Test
    void shouldAtualizarUsuarioComSucesso() {
        EnderecoDTO enderecoDTO = new EnderecoDTO(
                "Rua Atualizada", "123", "Apto 1", "Centro", "Cidade", "Estado", "12345-678"
        );

        UsuarioRequest usuarioAtualizado = new UsuarioRequest("nome", "email@email.com", Perfil.PROPRIETARIO, "login123", "senha123", enderecoDTO);

        UsuarioResponse response = new UsuarioResponse(
                1L,
                "Nome Atualizado",
                Perfil.PROPRIETARIO,
                "novo@email.com",
                "novologin",
                enderecoDTO,
                LocalDate.now()
        );

        when(usuarioService.update(eq(1L), any(UsuarioEditRequest.class))).thenReturn(response);

        given()
                .contentType(ContentType.JSON)
                .body(usuarioAtualizado)
                .when()
                .put("/api/v1/usuarios/1")
                .then()
                .statusCode(200)
                .log().body()
                .body("data", notNullValue())
                .body("data.nome", equalTo("Nome Atualizado"))
                .body("data.email", equalTo("novo@email.com"))
                .body("data.login", equalTo("novologin"));
    }

    @Test
    void shouldDeletarUsuarioComSucesso() {
        doNothing().when(usuarioService).delete(1L);

        given()
                .when()
                .delete("/api/v1/usuarios/1")
                .then()
                .statusCode(200)
                .log().body()
                .body("status", equalTo("SUCCESS"))
                .body("message", equalTo("Usuário removido com sucesso"));
    }

    @Test
    void shouldLoginComSucesso() {
        UsuarioLoginRequest loginRequest = new UsuarioLoginRequest("login123", "senha123");

        when(usuarioService.authenticate(any(UsuarioLoginRequest.class))).thenReturn(true);

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/v1/usuarios/login")
                .then()
                .statusCode(200)
                .log().body()
                .body("status", equalTo("SUCCESS"))
                .body("data", equalTo(true))
                .body("message", equalTo("Autenticação bem-sucedida"));
    }

    @Test
    void shouldFailWhenLoginWithWrongPwd() {
        UsuarioLoginRequest loginRequest = new UsuarioLoginRequest("login123", "senha123");

        when(usuarioService.authenticate(any(UsuarioLoginRequest.class))).thenReturn(false);

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/v1/usuarios/login")
                .then()
                .statusCode(200)
                .log().body()
                .body("status", equalTo("ERROR"))
                .body("message", equalTo("Falha na autenticação"));
    }

    @Test
    void shouldFailWhenLoginNotFound() {
        UsuarioLoginRequest loginRequest = new UsuarioLoginRequest("login123", "senha123");

        when(usuarioService.authenticate(any(UsuarioLoginRequest.class)))
                .thenThrow(new AuthenticationException("Login não encontrado"));

        given()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/api/v1/usuarios/login")
                .then()
                .statusCode(401)
                .log().body()
                .body("message", equalTo("Login não encontrado"));
    }

    @Test
    void shouldRetornar404QuandoUsuarioNaoEncontrado() {
        when(usuarioService.findById(999L))
                .thenThrow(new ResourceNotFoundException("Usuário não encontrado com id: 999"));

        given()
                .accept(ContentType.JSON)
                .when()
                .get("/api/v1/usuarios/999")
                .then()
                .statusCode(404)
                .log().body()
                .body("message", containsString("Usuário não encontrado com id: 999"));
    }

    @Test
    void shouldRetornar409QuandoLoginJaExiste() {

        EnderecoDTO enderecoDTO = new EnderecoDTO(
                "Rua Atualizada", "123", "Apto 1", "Centro", "Cidade", "Estado", "12345-678"
        );
        UsuarioRequest request = new UsuarioRequest("nome", "email@email.com", Perfil.PROPRIETARIO, "login123", "senha123", enderecoDTO);


        when(usuarioService.save(any(UsuarioRequest.class)))
                .thenThrow(new DuplicateResourceException("Login já está em uso"));

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/usuarios")
                .then()
                .statusCode(409)
                .log().body()
                .body("message", equalTo("Login já está em uso"));
    }

    private String getContentFromFile(String file) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file)) {
            assert inputStream != null;
            return new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CustomComparator buildCustomComparatorIgnoreFields(List<String> fieldsToIgnore) {
        List<Customization> customizations = fieldsToIgnore.stream()
                .map(field -> new Customization(field, (o1, o2) -> true))
                .toList();

        return new CustomComparator(LENIENT, customizations.toArray(new Customization[0]));
    }
}
