package br.com.fiap.tech_challenge_2.interfaces.controller.handler;

import br.com.fiap.tech_challenge_2.interfaces.exception.DuplicateResourceException;
import br.com.fiap.tech_challenge_2.interfaces.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionHandlerTest {

    private ControllerExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ControllerExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/usuarios");
        request.setMethod("POST");
        webRequest = new ServletWebRequest(request);
    }

    @Test
    void testHandleResourceNotFoundException() {
        // Given
        ResourceNotFoundException exception = new ResourceNotFoundException("Usuário não encontrado");

        // When
        ResponseEntity<ApiError> response = exceptionHandler.handleResourceNotFoundException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Usuário não encontrado", response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("uri=/api/usuarios", response.getBody().getPath());
    }

    @Test
    void testHandleDuplicateResourceException() {
        // Given
        DuplicateResourceException exception = new DuplicateResourceException("Login já está em uso");

        // When
        ResponseEntity<ApiError> response = exceptionHandler.handleDuplicateResourceException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Login já está em uso", response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getStatus());
        assertEquals("uri=/api/usuarios", response.getBody().getPath());
    }

    @Test
    void testHandleExceptionWithEmptyPath() {
        // Given
        ResourceNotFoundException exception = new ResourceNotFoundException("Teste");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("");
        request.setMethod("GET");
        WebRequest emptyPathRequest = new ServletWebRequest(request);

        // When
        ResponseEntity<ApiError> response = exceptionHandler.handleResourceNotFoundException(exception, emptyPathRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Teste", response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("uri=", response.getBody().getPath());
    }
} 