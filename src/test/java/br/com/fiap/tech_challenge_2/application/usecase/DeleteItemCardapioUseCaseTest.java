package br.com.fiap.tech_challenge_2.application.usecase;

import br.com.fiap.tech_challenge_2.application.usecase.impl.DeleteItemCardapioUseCaseImpl;
import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
import br.com.fiap.tech_challenge_2.domain.service.ItemCardapioDomainService;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteItemCardapioUseCaseTest {

    @Mock
    private ItemCardapioDomainService itemCardapioDomainService;

    @InjectMocks
    DeleteItemCardapioUseCaseImpl deleteItemCardapioUseCase;

    private ItemCardapio itemCardapio;

    @BeforeEach
    void setUp() {
        itemCardapio = new ItemCardapio();
        itemCardapio.setId(1L);
        itemCardapio.setNome("pao com carne");
        itemCardapio.setDescricao("descricao");
        itemCardapio.setPreco(BigDecimal.valueOf(10.99));
        itemCardapio.setDisponivelApenasNoLocal(true);
        itemCardapio.setFotoPath('/' + itemCardapio.getId() + ".jpg");
    }

    @Test
    void testExecute_Success() {
        when(itemCardapioDomainService.findItemCardapioById(1L)).thenReturn(Optional.of(itemCardapio));
        doNothing().when(itemCardapioDomainService).deleteItemCardapio(1L);

        assertDoesNotThrow(() -> deleteItemCardapioUseCase.execute(1L));

        verify(itemCardapioDomainService).findItemCardapioById(1L);
        verify(itemCardapioDomainService).deleteItemCardapio(1L);
    }

    @Test
    void testExecute_ItemNotFound() {
        // Given
        when(itemCardapioDomainService.findItemCardapioById(999L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            deleteItemCardapioUseCase.execute(999L);
        });

        assertEquals("Item não encontrado", exception.getMessage());

        verify(itemCardapioDomainService).findItemCardapioById(999L);
        verify(itemCardapioDomainService, never()).deleteItemCardapio(any());
    }

    @Test
    void testExecute_NullId() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            deleteItemCardapioUseCase.execute(null);
        });

        assertEquals("ID cannot be null", exception.getMessage());

        verify(itemCardapioDomainService, never()).findItemCardapioById(any());
        verify(itemCardapioDomainService, never()).deleteItemCardapio(any());
    }

    @Test
    void testExecute_DomainServiceThrowsException() {
        // Given
        when(itemCardapioDomainService.findItemCardapioById(1L)).thenReturn(Optional.of(itemCardapio));
        doThrow(new RuntimeException("Database error")).when(itemCardapioDomainService).deleteItemCardapio(1L);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deleteItemCardapioUseCase.execute(1L);
        });

        assertEquals("Database error", exception.getMessage());

        verify(itemCardapioDomainService).findItemCardapioById(1L);
        verify(itemCardapioDomainService).deleteItemCardapio(1L);
    }
}