package br.com.fiap.tech_challenge_2.interfaces.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

}