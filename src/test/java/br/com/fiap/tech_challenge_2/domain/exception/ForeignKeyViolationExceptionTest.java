package br.com.fiap.tech_challenge_2.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForeignKeyViolationExceptionTest {

    @Test
    void quandoCriarExceptionComMensagem_DeveManterMensagem() {
        // Arrange
        String mensagem = "Erro de violação de chave estrangeira";

        // Act
        ForeignKeyViolationException exception = new ForeignKeyViolationException(mensagem);

        // Assert
        assertEquals(mensagem, exception.getMessage());
    }

    @Test
    void quandoCriarException_DeveSerUmaRuntimeException() {
        // Act
        ForeignKeyViolationException exception = new ForeignKeyViolationException("mensagem");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void quandoCriarExceptionComMensagemNula_DeveAceitarMensagemNula() {
        // Act
        ForeignKeyViolationException exception = new ForeignKeyViolationException(null);

        // Assert
        assertNull(exception.getMessage());
    }

    @Test
    void quandoCriarExceptionComMensagemVazia_DeveManterMensagemVazia() {
        // Arrange
        String mensagemVazia = "";

        // Act
        ForeignKeyViolationException exception = new ForeignKeyViolationException(mensagemVazia);

        // Assert
        assertEquals(mensagemVazia, exception.getMessage());
    }

    @Test
    void quandoLancarException_DevePropagar() {
        // Act & Assert
        assertThrows(ForeignKeyViolationException.class, () -> {
            throw new ForeignKeyViolationException("Erro de chave estrangeira");
        });
    }
}
