package br.com.fiap.tech_challenge_2.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestauranteTest {

    private Restaurante restaurante;
    private Usuario dono;
    private Endereco endereco;
    private ItemCardapio itemCardapio;

    @BeforeEach
    void setUp() {
        dono = new Usuario();
        dono.setId(1L);
        dono.setNome("João Silva");
        dono.setEmail("joao@email.com");
        dono.setLogin("joao123");

        endereco = new Endereco();
        endereco.setLogradouro("Rua do Restaurante");
        endereco.setNumero("100");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01234567");

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setTipoCozinha("Italiana");
        restaurante.setHorarioFuncionamento("12:00-22:00");
        restaurante.setEndereco(endereco);
        restaurante.setDono(dono);

        itemCardapio = new ItemCardapio();
        itemCardapio.setId(1L);
        itemCardapio.setNome("Pizza Margherita");
        itemCardapio.setDescricao("Pizza tradicional italiana");
        itemCardapio.setPreco(25.90);
        itemCardapio.setDisponivelApenasNoLocal(false);
    }

    @Test
    void testIsValidForRegistration_Valid() {
        // When
        boolean result = restaurante.isValidForRegistration();

        // Then
        assertTrue(result);
    }

    @Test
    void testIsValidForRegistration_NullNome() {
        // Given
        restaurante.setNome(null);

        // When
        boolean result = restaurante.isValidForRegistration();

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValidForRegistration_EmptyNome() {
        // Given
        restaurante.setNome("");

        // When
        boolean result = restaurante.isValidForRegistration();

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValidForRegistration_BlankNome() {
        // Given
        restaurante.setNome("   ");

        // When
        boolean result = restaurante.isValidForRegistration();

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValidForRegistration_NullTipoCozinha() {
        // Given
        restaurante.setTipoCozinha(null);

        // When
        boolean result = restaurante.isValidForRegistration();

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValidForRegistration_NullHorarioFuncionamento() {
        // Given
        restaurante.setHorarioFuncionamento(null);

        // When
        boolean result = restaurante.isValidForRegistration();

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValidForRegistration_NullDono() {
        // Given
        restaurante.setDono(null);

        // When
        boolean result = restaurante.isValidForRegistration();

        // Then
        assertFalse(result);
    }

    @Test
    void testAddMenuItem_Success() {
        // When
        restaurante.addMenuItem(itemCardapio);

        // Then
        assertTrue(restaurante.hasMenuItems());
        assertEquals(1, restaurante.getCardapio().size());
        assertEquals(itemCardapio, restaurante.getCardapio().get(0));
        assertEquals(restaurante, itemCardapio.getRestaurante());
    }

    @Test
    void testAddMenuItem_NullItem() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            restaurante.addMenuItem(null);
        });

        assertEquals("Item cannot be null", exception.getMessage());
    }

    @Test
    void testAddMenuItem_MultipleItems() {
        // Given
        ItemCardapio item2 = new ItemCardapio();
        item2.setId(2L);
        item2.setNome("Pizza Pepperoni");

        // When
        restaurante.addMenuItem(itemCardapio);
        restaurante.addMenuItem(item2);

        // Then
        assertTrue(restaurante.hasMenuItems());
        assertEquals(2, restaurante.getCardapio().size());
    }

    @Test
    void testRemoveMenuItem_Success() {
        // Given
        restaurante.addMenuItem(itemCardapio);

        // When
        restaurante.removeMenuItem(1L);

        // Then
        assertFalse(restaurante.hasMenuItems());
        assertTrue(restaurante.getCardapio().isEmpty());
    }

    @Test
    void testRemoveMenuItem_ItemNotFound() {
        // Given
        restaurante.addMenuItem(itemCardapio);

        // When
        restaurante.removeMenuItem(999L);

        // Then
        assertTrue(restaurante.hasMenuItems());
        assertEquals(1, restaurante.getCardapio().size());
    }

    @Test
    void testRemoveMenuItem_EmptyCardapio() {
        // When
        restaurante.removeMenuItem(1L);

        // Then
        assertFalse(restaurante.hasMenuItems());
    }

    @Test
    void testGetAvailableMenuItems_AllAvailable() {
        // Given
        itemCardapio.setDisponivelApenasNoLocal(false);
        restaurante.addMenuItem(itemCardapio);

        // When
        List<ItemCardapio> availableItems = restaurante.getAvailableMenuItems();

        // Then
        assertEquals(1, availableItems.size());
        assertEquals(itemCardapio, availableItems.get(0));
    }

    @Test
    void testGetAvailableMenuItems_NoneAvailable() {
        // Given
        itemCardapio.setDisponivelApenasNoLocal(true);
        restaurante.addMenuItem(itemCardapio);

        // When
        List<ItemCardapio> availableItems = restaurante.getAvailableMenuItems();

        // Then
        assertTrue(availableItems.isEmpty());
    }

    @Test
    void testGetAvailableMenuItems_MixedAvailability() {
        // Given
        ItemCardapio item2 = new ItemCardapio();
        item2.setId(2L);
        item2.setNome("Pizza Pepperoni");
        item2.setDisponivelApenasNoLocal(false);

        itemCardapio.setDisponivelApenasNoLocal(true);
        restaurante.addMenuItem(itemCardapio);
        restaurante.addMenuItem(item2);

        // When
        List<ItemCardapio> availableItems = restaurante.getAvailableMenuItems();

        // Then
        assertEquals(1, availableItems.size());
        assertEquals(item2, availableItems.get(0));
    }

    @Test
    void testGetAvailableMenuItems_EmptyCardapio() {
        // When
        List<ItemCardapio> availableItems = restaurante.getAvailableMenuItems();

        // Then
        assertTrue(availableItems.isEmpty());
    }

    @Test
    void testHasMenuItems_True() {
        // Given
        restaurante.addMenuItem(itemCardapio);

        // When
        boolean result = restaurante.hasMenuItems();

        // Then
        assertTrue(result);
    }

    @Test
    void testHasMenuItems_False() {
        // When
        boolean result = restaurante.hasMenuItems();

        // Then
        assertFalse(result);
    }

    @Test
    void testUpdateRestaurantInfo_Success() {
        // When
        restaurante.updateRestaurantInfo("Novo Nome", "Japonesa", "18:00-23:00");

        // Then
        assertEquals("Novo Nome", restaurante.getNome());
        assertEquals("Japonesa", restaurante.getTipoCozinha());
        assertEquals("18:00-23:00", restaurante.getHorarioFuncionamento());
    }

    @Test
    void testUpdateRestaurantInfo_PartialUpdate() {
        // When
        restaurante.updateRestaurantInfo("Novo Nome", null, null);

        // Then
        assertEquals("Novo Nome", restaurante.getNome());
        assertEquals("Italiana", restaurante.getTipoCozinha()); // unchanged
        assertEquals("12:00-22:00", restaurante.getHorarioFuncionamento()); // unchanged
    }

    @Test
    void testUpdateRestaurantInfo_EmptyValues() {
        // When
        restaurante.updateRestaurantInfo("", "", "");

        // Then
        assertEquals("Restaurante Teste", restaurante.getNome()); // unchanged
        assertEquals("Italiana", restaurante.getTipoCozinha()); // unchanged
        assertEquals("12:00-22:00", restaurante.getHorarioFuncionamento()); // unchanged
    }

    @Test
    void testUpdateRestaurantInfo_BlankValues() {
        // When
        restaurante.updateRestaurantInfo("   ", "   ", "   ");

        // Then
        assertEquals("Restaurante Teste", restaurante.getNome()); // unchanged
        assertEquals("Italiana", restaurante.getTipoCozinha()); // unchanged
        assertEquals("12:00-22:00", restaurante.getHorarioFuncionamento()); // unchanged
    }
} 