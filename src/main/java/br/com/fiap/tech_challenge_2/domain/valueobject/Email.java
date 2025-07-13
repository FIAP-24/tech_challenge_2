package br.com.fiap.tech_challenge_2.domain.valueobject;

import java.util.regex.Pattern;

public class Email {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private final String value;
    
    public Email(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
        this.value = value.toLowerCase().trim();
    }
    
    public static boolean isValid(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Email email = (Email) obj;
        return value.equals(email.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
} 