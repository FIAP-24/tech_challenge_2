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
class TipoUsuarioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarEListarTipoUsuario() throws Exception {
        String tipoUsuarioJson = "{\"nome\":\"ADMINISTRADOR\"}";

        // Cria tipo de usuário
        mockMvc.perform(post("/api/v1/tipos-usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tipoUsuarioJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nome").value("ADMINISTRADOR"));

        // Lista tipos de usuário
        mockMvc.perform(get("/api/v1/tipos-usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }
} 