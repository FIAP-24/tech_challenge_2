package br.com.fiap.tech_challenge_2.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestauranteNotFoundExceptionTest {

    @Test
    void quandoCriarExceptionComMensagem_DeveManterMensagem() {
        // Arrange
        String mensagem = "Restaurante não encontrado";

        // Act
        RestauranteNotFoundException exception = new RestauranteNotFoundException(mensagem);

        // Assert
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    void quandoCriarExceptionComId_DeveMontarMensagemCorreta() {
        // Arrange
        Long id = 123L;

        // Act
        RestauranteNotFoundException exception = new RestauranteNotFoundException(id);

        // Assert
        assertEquals("Restaurante não encontrado com id: 123", exception.getMessage());
    }

    @Test
    void quandoCriarException_DeveSerUmaRuntimeException() {
        // Act
        RestauranteNotFoundException exception = new RestauranteNotFoundException("mensagem");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void quandoCriarExceptionComIdNulo_DeveGerarMensagemComNull() {
        // Act
        RestauranteNotFoundException exception = new RestauranteNotFoundException((Long) null);

        // Assert
        assertEquals("Restaurante não encontrado com id: null", exception.getMessage());
    }

    @Test
    void quandoCriarExceptionComMensagemNula_DeveAceitarMensagemNula() {
        // Act
        RestauranteNotFoundException exception = new RestauranteNotFoundException((String) null);

        // Assert
        assertNull(exception.getMessage());
    }

    @Test
    void quandoLancarException_DevePropagar() {
        // Act & Assert
        assertThrows(RestauranteNotFoundException.class, () -> {
            throw new RestauranteNotFoundException("Restaurante não encontrado");
        });
    }

    @Test
    void quandoLancarExceptionComId_DevePropagar() {
        // Arrange
        Long id = 456L;

        // Act & Assert
        RestauranteNotFoundException exception = assertThrows(RestauranteNotFoundException.class, () -> {
            throw new RestauranteNotFoundException(id);
        });

        // Assert
        assertEquals("Restaurante não encontrado com id: 456", exception.getMessage());
    }
}
