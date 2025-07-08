package br.com.fiap.tech_challenge_2.utils;

import br.com.fiap.tech_challenge_2.infrastruture.utils.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordHasherTest {
    private PasswordHasher passwordHasher;

    @BeforeEach
    public void setUp() {
        passwordHasher = new PasswordHasher();
    }

    @Test
    public void testHashPassword_NotNullAndDifferentFromRaw() {
        String rawPassword = "Senha123!";
        String hashedPassword = passwordHasher.hashPassword(rawPassword);

        assertNotNull(hashedPassword, "Hashed password should not be null");
        assertNotEquals(rawPassword, hashedPassword, "Hashed password should not match raw password");
    }

    @Test
    public void testVerifyPassword_CorrectPassword() {
        String rawPassword = "correctPassword!";
        String hashedPassword = passwordHasher.hashPassword(rawPassword);

        assertTrue(passwordHasher.verifyPassword(rawPassword, hashedPassword),
                "Verification should return true for correct password");
    }

    @Test
    public void testVerifyPassword_WrongPassword() {
        String rawPassword = "correctPassword!";
        String wrongPassword = "wrongPassword123!";
        String hashedPassword = passwordHasher.hashPassword(rawPassword);

        assertFalse(passwordHasher.verifyPassword(wrongPassword, hashedPassword),
                "Verification should return false for incorrect password");
    }
}
