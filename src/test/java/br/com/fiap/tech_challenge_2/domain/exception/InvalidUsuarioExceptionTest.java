package br.com.fiap.tech_challenge_2.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidUsuarioExceptionTest {

    @Test
    void quandoCriarExceptionComMensagem_DeveManterMensagem() {
        // Arrange
        String mensagem = "Usuário inválido";

        // Act
        InvalidUsuarioException exception = new InvalidUsuarioException(mensagem);

        // Assert
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    void quandoCriarExceptionComMensagemECausa_DeveManterAmbos() {
        // Arrange
        String mensagem = "Usuário inválido";
        Throwable causa = new RuntimeException("Causa original");

        // Act
        InvalidUsuarioException exception = new InvalidUsuarioException(mensagem, causa);

        // Assert
        assertEquals(mensagem, exception.getMessage());
        assertEquals(causa, exception.getCause());
    }

    @Test
    void quandoCriarException_DeveSerUmaRuntimeException() {
        // Act
        InvalidUsuarioException exception = new InvalidUsuarioException("mensagem");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void quandoCriarExceptionComMensagemNula_DeveAceitarMensagemNula() {
        // Act
        InvalidUsuarioException exception = new InvalidUsuarioException(null);

        // Assert
        assertNull(exception.getMessage());
    }

    @Test
    void quandoCriarExceptionComCausaNula_DeveAceitarCausaNula() {
        // Act
        InvalidUsuarioException exception = new InvalidUsuarioException("mensagem", null);

        // Assert
        assertNull(exception.getCause());
    }

    @Test
    void quandoLancarException_DevePropagar() {
        // Act & Assert
        assertThrows(InvalidUsuarioException.class, () -> {
            throw new InvalidUsuarioException("Erro de validação");
        });
    }

    @Test
    void quandoLancarExceptionComCausa_DevePropagar() {
        // Arrange
        RuntimeException causaOriginal = new RuntimeException("Causa original");

        // Act & Assert
        InvalidUsuarioException exception = assertThrows(InvalidUsuarioException.class, () -> {
            throw new InvalidUsuarioException("Erro de validação", causaOriginal);
        });

        // Verify both message and cause
        assertEquals("Erro de validação", exception.getMessage());
        assertEquals(causaOriginal, exception.getCause());
    }
}
