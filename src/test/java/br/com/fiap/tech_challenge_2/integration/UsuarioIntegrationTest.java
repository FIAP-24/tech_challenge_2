package br.com.fiap.tech_challenge_2.integration;

import br.com.fiap.tech_challenge_2.application.dto.request.EnderecoDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarEListarUsuario() throws Exception {
        String usuarioJson = """
            {
                "nome": "João Teste",
                "email": "joao_unico@email.com",
                "tipoUsuarioId": 1,
                "login": "joaounico",
                "senha": "123456",
                "endereco": {
                    "logradouro": "Rua Teste",
                    "numero": "123",
                    "complemento": "Apto 1",
                    "bairro": "Centro",
                    "cidade": "São Paulo",
                    "estado": "SP",
                    "cep": "01234567"
                }
            }
            """;

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nome").value("João Teste"));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }
} 