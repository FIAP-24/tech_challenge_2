package br.com.fiap.tech_challenge_2.infrastruture.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasher {

    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordHasher() {
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    /**
     * Gera um hash seguro da senha usando BCrypt
     */
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * Verifica se a senha informada corresponde ao hash armazenado
     */
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

}
