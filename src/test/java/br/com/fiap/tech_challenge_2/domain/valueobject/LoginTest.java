package br.com.fiap.tech_challenge_2.domain.valueobject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @Test
    void deveCriarLoginValido() {
        String loginValido = "usuario123";
        Login login = new Login(loginValido);
        
        assertEquals(loginValido, login.getValue());
    }

    @Test
    void deveLancarExcecaoParaLoginNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Login(null);
        });
    }

    @Test
    void deveLancarExcecaoParaLoginVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Login("");
        });
    }

    @Test
    void deveLancarExcecaoParaLoginComEspacos() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Login("  ");
        });
    }

    @Test
    void deveLancarExcecaoParaLoginMuitoCurto() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Login("ab");
        });
    }

    @Test
    void deveLancarExcecaoParaLoginMuitoLongo() {
        String loginLongo = "a".repeat(51);
        assertThrows(IllegalArgumentException.class, () -> {
            new Login(loginLongo);
        });
    }

    @Test
    void deveSerIgualQuandoLoginsSaoIguais() {
        Login login1 = new Login("usuario123");
        Login login2 = new Login("usuario123");
        
        assertEquals(login1, login2);
        assertEquals(login1.hashCode(), login2.hashCode());
    }

    @Test
    void deveSerDiferenteQuandoLoginsSaoDiferentes() {
        Login login1 = new Login("usuario1");
        Login login2 = new Login("usuario2");
        
        assertNotEquals(login1, login2);
    }

    @Test
    void deveRetornarStringCorretaNoToString() {
        String loginString = "usuario123";
        Login login = new Login(loginString);
        
        assertEquals(loginString, login.toString());
    }
} 