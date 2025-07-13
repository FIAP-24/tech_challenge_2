package br.com.fiap.tech_challenge_2.domain.exception;

public class RestauranteNotFoundException extends RuntimeException {
    
    public RestauranteNotFoundException(String message) {
        super(message);
    }
    
    public RestauranteNotFoundException(Long id) {
        super("Restaurante não encontrado com id: " + id);
    }
} 