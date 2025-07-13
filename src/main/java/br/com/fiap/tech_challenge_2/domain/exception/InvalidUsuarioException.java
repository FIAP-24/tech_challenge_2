package br.com.fiap.tech_challenge_2.domain.exception;

public class InvalidUsuarioException extends RuntimeException {
    
    public InvalidUsuarioException(String message) {
        super(message);
    }
    
    public InvalidUsuarioException(String message, Throwable cause) {
        super(message, cause);
    }
} 