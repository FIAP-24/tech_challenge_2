package br.com.fiap.tech_challenge_2.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuario;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        endereco = new Endereco();
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero("123");
        endereco.setComplemento("Apto 1");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01234567");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joaosilva");
        usuario.setSenha("senha123");
        usuario.setEndereco(endereco);
        usuario.setDataUpdate(LocalDate.now());
    }

    @Test
    void deveSerValidoParaRegistro() {
        assertTrue(usuario.isValidForRegistration());
    }

    @Test
    void deveSerInvalidoParaRegistroQuandoNomeNulo() {
        usuario.setNome(null);
        assertFalse(usuario.isValidForRegistration());
    }

    @Test
    void deveSerInvalidoParaRegistroQuandoNomeVazio() {
        usuario.setNome("");
        assertFalse(usuario.isValidForRegistration());
    }

    @Test
    void deveSerInvalidoParaRegistroQuandoNomeComEspacos() {
        usuario.setNome("   ");
        assertFalse(usuario.isValidForRegistration());
    }

    @Test
    void deveSerInvalidoParaRegistroQuandoLoginNulo() {
        usuario.setLogin(null);
        assertFalse(usuario.isValidForRegistration());
    }

    @Test
    void deveSerInvalidoParaRegistroQuandoLoginVazio() {
        usuario.setLogin("");
        assertFalse(usuario.isValidForRegistration());
    }

    @Test
    void deveSerInvalidoParaRegistroQuandoSenhaNula() {
        usuario.setSenha(null);
        assertFalse(usuario.isValidForRegistration());
    }

    @Test
    void deveSerInvalidoParaRegistroQuandoSenhaVazia() {
        usuario.setSenha("");
        assertFalse(usuario.isValidForRegistration());
    }

    @Test
    void deveAtualizarSenha() {
        String novaSenha = "novaSenha123";
        LocalDate dataAntes = usuario.getDataUpdate();
        
        usuario.updatePassword(novaSenha);
        
        assertEquals(novaSenha, usuario.getSenha());
        assertTrue(usuario.getDataUpdate().isAfter(dataAntes) || usuario.getDataUpdate().isEqual(dataAntes));
    }

    @Test
    void deveAtualizarPerfilComNome() {
        String novoNome = "João Silva Atualizado";
        LocalDate dataAntes = usuario.getDataUpdate();
        
        usuario.updateProfile(novoNome, null);
        
        assertEquals(novoNome, usuario.getNome());
        assertTrue(usuario.getDataUpdate().isAfter(dataAntes) || usuario.getDataUpdate().isEqual(dataAntes));
    }

    @Test
    void deveAtualizarPerfilComEmail() {
        String novoEmail = "joao.atualizado@email.com";
        LocalDate dataAntes = usuario.getDataUpdate();
        
        usuario.updateProfile(null, novoEmail);
        
        assertEquals(novoEmail, usuario.getEmail());
        assertTrue(usuario.getDataUpdate().isAfter(dataAntes) || usuario.getDataUpdate().isEqual(dataAntes));
    }

    @Test
    void deveAtualizarPerfilComNomeEEmail() {
        String novoNome = "João Silva Atualizado";
        String novoEmail = "joao.atualizado@email.com";
        LocalDate dataAntes = usuario.getDataUpdate();
        
        usuario.updateProfile(novoNome, novoEmail);
        
        assertEquals(novoNome, usuario.getNome());
        assertEquals(novoEmail, usuario.getEmail());
        assertTrue(usuario.getDataUpdate().isAfter(dataAntes) || usuario.getDataUpdate().isEqual(dataAntes));
    }

    @Test
    void deveIgnorarNomeVazioNaAtualizacao() {
        String nomeOriginal = usuario.getNome();
        LocalDate dataAntes = usuario.getDataUpdate();
        
        usuario.updateProfile("", null);
        
        assertEquals(nomeOriginal, usuario.getNome());
        assertTrue(usuario.getDataUpdate().isAfter(dataAntes) || usuario.getDataUpdate().isEqual(dataAntes));
    }

    @Test
    void deveIgnorarEmailVazioNaAtualizacao() {
        String emailOriginal = usuario.getEmail();
        LocalDate dataAntes = usuario.getDataUpdate();
        
        usuario.updateProfile(null, "");
        
        assertEquals(emailOriginal, usuario.getEmail());
        assertTrue(usuario.getDataUpdate().isAfter(dataAntes) || usuario.getDataUpdate().isEqual(dataAntes));
    }

    @Test
    void deveIgnorarNomeNuloNaAtualizacao() {
        String nomeOriginal = usuario.getNome();
        LocalDate dataAntes = usuario.getDataUpdate();
        
        usuario.updateProfile(null, null);
        
        assertEquals(nomeOriginal, usuario.getNome());
        assertTrue(usuario.getDataUpdate().isAfter(dataAntes) || usuario.getDataUpdate().isEqual(dataAntes));
    }
} 