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
class ItemCardapioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarEListarItemCardapio() throws Exception {
        // Primeiro, criar um usuário
        String usuarioJson = """
            {
                "nome": "João Cardapio",
                "email": "joao_cardapio@email.com",
                "perfil": "PROPRIETARIO",
                "login": "joaocardapio",
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

        String usuarioResponse = mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extrair o ID do usuário criado (assumindo que o response contém o ID)
        // Por simplicidade, vamos usar ID 1
        Long usuarioId = 1L;

        // Criar um restaurante
        String restauranteJson = """
            {
                "nome": "Restaurante Cardápio Teste",
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

        String restauranteResponse = mockMvc.perform(post("/api/v1/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(restauranteJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extrair o ID do restaurante criado (assumindo que o response contém o ID)
        // Por simplicidade, vamos usar ID 1
        Long restauranteId = 1L;

        // Agora criar o item do cardápio
        String itemCardapioJson = """
            {
                "nome": "Pizza Margherita",
                "descricao": "Pizza tradicional italiana",
                "preco": 45.00,
                "disponivelApenasNoLocal": false,
                "fotoPath": "/imagens/pizza.jpg",
                "restauranteId": %d
            }
            """.formatted(restauranteId);

        mockMvc.perform(post("/api/v1/itens-cardapio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemCardapioJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nome").value("Pizza Margherita"))
                .andExpect(jsonPath("$.data.preco").value(45.00));

        // Listar itens do cardápio
        mockMvc.perform(get("/api/v1/itens-cardapio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].nome").value("Pizza Margherita"));
    }
} 