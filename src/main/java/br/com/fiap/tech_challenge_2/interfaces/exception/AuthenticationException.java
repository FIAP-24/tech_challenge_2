package br.com.fiap.tech_challenge_2.interfaces.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

}