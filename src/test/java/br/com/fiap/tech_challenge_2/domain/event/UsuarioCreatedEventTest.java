package br.com.fiap.tech_challenge_2.domain.event;

import br.com.fiap.tech_challenge_2.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioCreatedEventTest {

    private Usuario usuario;
    private UsuarioCreatedEvent event;

    @BeforeEach
    void setUp() {
        // Arrange
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Test User");
        usuario.setEmail("test@email.com");
        usuario.setLogin("testuser");

        event = new UsuarioCreatedEvent(usuario);
    }

    @Test
    void quandoCriarEvento_DeveManterReferenciaAoUsuario() {
        // Assert
        assertNotNull(event.getUsuario());
        assertEquals(usuario, event.getUsuario());
        assertEquals(1L, event.getUsuario().getId());
        assertEquals("Test User", event.getUsuario().getNome());
        assertEquals("test@email.com", event.getUsuario().getEmail());
        assertEquals("testuser", event.getUsuario().getLogin());
    }

    @Test
    void quandoCriarEvento_DeveDefinirDataCriacaoAutomaticamente() {
        // Arrange
        LocalDateTime antes = LocalDateTime.now().minusSeconds(1);

        // Act
        UsuarioCreatedEvent novoEvent = new UsuarioCreatedEvent(usuario);
        LocalDateTime depois = LocalDateTime.now().plusSeconds(1);

        // Assert
        assertNotNull(novoEvent.getCreatedAt());
        assertTrue(novoEvent.getCreatedAt().isAfter(antes) || novoEvent.getCreatedAt().equals(antes));
        assertTrue(novoEvent.getCreatedAt().isBefore(depois) || novoEvent.getCreatedAt().equals(depois));
    }

    @Test
    void quandoCriarDoisEventos_DevemTerTimestampsDiferentes() {
        // Arrange
        UsuarioCreatedEvent event1 = new UsuarioCreatedEvent(usuario);

        // Simula uma pequena pausa para garantir timestamps diferentes
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            fail("Teste interrompido inesperadamente");
        }

        // Act
        UsuarioCreatedEvent event2 = new UsuarioCreatedEvent(usuario);

        // Assert
        assertNotEquals(event1.getCreatedAt(), event2.getCreatedAt());
        assertTrue(event2.getCreatedAt().isAfter(event1.getCreatedAt()));
    }

    @Test
    void getUsuario_DeveRetornarMesmaInstanciaPassadaNoConstrutor() {
        // Act
        Usuario usuarioRetornado = event.getUsuario();

        // Assert
        assertSame(usuario, usuarioRetornado);
    }
}
