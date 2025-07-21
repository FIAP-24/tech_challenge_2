package br.com.fiap.tech_challenge_2.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RestauranteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarEListarRestaurante() throws Exception {
        // Primeiro, criar um usuário
        String usuarioJson = """
            {
                "nome": "Proprietário Teste",
                "email": "proprietario.teste@email.com",
                "tipoUsuarioId": 1,
                "login": "proprietario_teste",
                "senha": "123456",
                "endereco": {
                    "logradouro": "Rua do Restaurante",
                    "numero": "100",
                    "complemento": "Loja 1",
                    "bairro": "Centro",
                    "cidade": "São Paulo",
                    "estado": "SP",
                    "cep": "01234567"
                }
            }
            """;

        String usuarioResponse = mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extrair o ID do usuário criado (assumindo que o response contém o ID)
        // Por simplicidade, vamos usar ID 1
        Long usuarioId = 1L;

        // Agora criar o restaurante
        String restauranteJson = """
            {
                "nome": "Restaurante Teste Integração",
                "endereco": {
                    "logradouro": "Rua Teste",
                    "numero": "123",
                    "complemento": "Apto 1",
                    "bairro": "Centro",
                    "cidade": "São Paulo",
                    "estado": "SP",
                    "cep": "01234567"
                },
                "tipoCozinha": "Italiana",
                "horarioFuncionamento": "Seg-Sex: 09:00-22:00",
                "donoId": %d
            }
            """.formatted(usuarioId);

        mockMvc.perform(post("/api/v1/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(restauranteJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nome").value("Restaurante Teste Integração"))
                .andExpect(jsonPath("$.data.tipoCozinha").value("Italiana"));

        // Listar restaurantes
        mockMvc.perform(get("/api/v1/restaurantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[?(@.nome == 'Restaurante Teste Integração')]").exists());
    }
} 