package br.com.fiap.tech_challenge_2.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemCardapioTest {

    private ItemCardapio itemCardapio;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        itemCardapio = new ItemCardapio();
        itemCardapio.setId(1L);
        itemCardapio.setNome("Pizza Margherita");
        itemCardapio.setDescricao("Pizza tradicional italiana");
        itemCardapio.setPreco(25.90);
        itemCardapio.setDisponivelApenasNoLocal(false);
        itemCardapio.setFotoPath("/fotos/pizza.jpg");
        itemCardapio.setRestaurante(restaurante);
    }

    @Test
    void testIsValidForRegistration_Valid() {
        // When
        boolean result = itemCardapio.isValidForRegistration();

        // Then
        assertTrue(result);
    }

    @Test
    void testIsValidForRegistration_NullNome() {
        // Given
        itemCardapio.setNome(null);

        // When
        boolean result = itemCardapio.isValidForRegistration();

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValidForRegistration_EmptyNome() {
        // Given
        itemCardapio.setNome("");

        // When
        boolean result = itemCardapio.isValidForRegistration();

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValidForRegistration_BlankNome() {
        // Given
        itemCardapio.setNome("   ");

        // When
        boolean result = itemCardapio.isValidForRegistration();

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValidForRegistration_NullPreco() {
        // Given
        itemCardapio.setPreco(null);

        // When
        boolean result = itemCardapio.isValidForRegistration();

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValidForRegistration_ZeroPreco() {
        // Given
        itemCardapio.setPreco(0.0);

        // When
        boolean result = itemCardapio.isValidForRegistration();

        // Then
        assertFalse(result);
    }

    @Test
    void testIsValidForRegistration_NegativePreco() {
        // Given
        itemCardapio.setPreco(-10.0);

        // When
        boolean result = itemCardapio.isValidForRegistration();

        // Then
        assertFalse(result);
    }

    @Test
    void testIsAvailable_True() {
        // Given
        itemCardapio.setDisponivelApenasNoLocal(false);

        // When
        boolean result = itemCardapio.isAvailable();

        // Then
        assertTrue(result);
    }

    @Test
    void testIsAvailable_False() {
        // Given
        itemCardapio.setDisponivelApenasNoLocal(true);

        // When
        boolean result = itemCardapio.isAvailable();

        // Then
        assertFalse(result);
    }

    @Test
    void testUpdatePrice_Success() {
        // When
        itemCardapio.updatePrice(30.50);

        // Then
        assertEquals(30.50, itemCardapio.getPreco());
    }

    @Test
    void testUpdatePrice_NullPrice() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemCardapio.updatePrice(null);
        });

        assertEquals("Price must be greater than zero", exception.getMessage());
    }

    @Test
    void testUpdatePrice_ZeroPrice() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemCardapio.updatePrice(0.0);
        });

        assertEquals("Price must be greater than zero", exception.getMessage());
    }

    @Test
    void testUpdatePrice_NegativePrice() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            itemCardapio.updatePrice(-10.0);
        });

        assertEquals("Price must be greater than zero", exception.getMessage());
    }

    @Test
    void testUpdateAvailability_Success() {
        // When
        itemCardapio.updateAvailability(true);

        // Then
        assertTrue(itemCardapio.isDisponivelApenasNoLocal());
    }

    @Test
    void testUpdateItemInfo_Success() {
        // When
        itemCardapio.updateItemInfo("Nova Pizza", "Nova descrição", 35.90);

        // Then
        assertEquals("Nova Pizza", itemCardapio.getNome());
        assertEquals("Nova descrição", itemCardapio.getDescricao());
        assertEquals(35.90, itemCardapio.getPreco());
    }

    @Test
    void testUpdateItemInfo_PartialUpdate() {
        // When
        itemCardapio.updateItemInfo("Nova Pizza", null, null);

        // Then
        assertEquals("Nova Pizza", itemCardapio.getNome());
        assertEquals("Pizza tradicional italiana", itemCardapio.getDescricao()); // unchanged
        assertEquals(25.90, itemCardapio.getPreco()); // unchanged
    }

    @Test
    void testUpdateItemInfo_EmptyValues() {
        // When
        itemCardapio.updateItemInfo("", "", 0.0);

        // Then
        assertEquals("Pizza Margherita", itemCardapio.getNome()); // unchanged
        assertEquals("", itemCardapio.getDescricao()); // trimmed empty string
        assertEquals(25.90, itemCardapio.getPreco()); // unchanged
    }

    @Test
    void testUpdateItemInfo_BlankValues() {
        // When
        itemCardapio.updateItemInfo("   ", "   ", 0.0);

        // Then
        assertEquals("Pizza Margherita", itemCardapio.getNome()); // unchanged
        assertEquals("", itemCardapio.getDescricao()); // trimmed blank string
        assertEquals(25.90, itemCardapio.getPreco()); // unchanged
    }

    @Test
    void testGetFormattedPrice_Success() {
        // When
        String result = itemCardapio.getFormattedPrice();

        // Then
        assertEquals("R$ 25.90", result);
    }

    @Test
    void testGetFormattedPrice_NullPrice() {
        // Given
        itemCardapio.setPreco(null);

        // When
        String result = itemCardapio.getFormattedPrice();

        // Then
        assertEquals("Preço não informado", result);
    }

    @Test
    void testGetFormattedPrice_ZeroPrice() {
        // Given
        itemCardapio.setPreco(0.0);

        // When
        String result = itemCardapio.getFormattedPrice();

        // Then
        assertEquals("R$ 0.00", result);
    }

    @Test
    void testGetFormattedPrice_DecimalPrice() {
        // Given
        itemCardapio.setPreco(25.99);

        // When
        String result = itemCardapio.getFormattedPrice();

        // Then
        assertEquals("R$ 25.99", result);
    }
} 