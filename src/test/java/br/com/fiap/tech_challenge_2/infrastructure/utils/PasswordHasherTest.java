package br.com.fiap.tech_challenge_2.infrastructure.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    @Test
    void testVerifyPassword_Success() {
        PasswordHasher hasher = new PasswordHasher();
        String rawPassword = "senhaSegura123";
        String hash = hasher.hashPassword(rawPassword);
        assertTrue(hasher.verifyPassword(rawPassword, hash));
    }

    @Test
    void testVerifyPassword_Failure() {
        PasswordHasher hasher = new PasswordHasher();
        String rawPassword = "senhaSegura123";
        String wrongPassword = "senhaErrada";
        String hash = hasher.hashPassword(rawPassword);
        assertFalse(hasher.verifyPassword(wrongPassword, hash));
    }

    @Test
    void testVerifyPassword_WithNull() {
        PasswordHasher hasher = new PasswordHasher();
        String hash = hasher.hashPassword("abc");
        assertThrows(IllegalArgumentException.class, () -> hasher.verifyPassword(null, hash));
    }
} 