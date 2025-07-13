package br.com.fiap.tech_challenge_2.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void deveCriarEmailValido() {
        String emailValido = "teste@email.com";
        Email email = new Email(emailValido);
        
        assertEquals(emailValido, email.getValue());
    }

    @Test
    void deveLancarExcecaoParaEmailInvalido() {
        String emailInvalido = "email-invalido";
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Email(emailInvalido);
        });
    }

    @Test
    void deveLancarExcecaoParaEmailNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Email(null);
        });
    }

    @Test
    void deveLancarExcecaoParaEmailVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Email("");
        });
    }

    @Test
    void deveLancarExcecaoParaEmailComEspacos() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Email("  ");
        });
    }

    @Test
    void deveSerIgualQuandoEmailsSaoIguais() {
        Email email1 = new Email("teste@email.com");
        Email email2 = new Email("teste@email.com");
        
        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void deveSerDiferenteQuandoEmailsSaoDiferentes() {
        Email email1 = new Email("teste1@email.com");
        Email email2 = new Email("teste2@email.com");
        
        assertNotEquals(email1, email2);
    }

    @Test
    void deveRetornarStringCorretaNoToString() {
        String emailString = "teste@email.com";
        Email email = new Email(emailString);
        
        assertEquals(emailString, email.toString());
    }
} 