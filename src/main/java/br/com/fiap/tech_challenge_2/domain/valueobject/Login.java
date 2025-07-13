package br.com.fiap.tech_challenge_2.domain.valueobject;

import java.util.regex.Pattern;

public class Login {
    
    private static final Pattern LOGIN_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    
    private final String value;
    
    public Login(String value) {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid login format: " + value);
        }
        this.value = value.toLowerCase().trim();
    }
    
    public static boolean isValid(String login) {
        return login != null && LOGIN_PATTERN.matcher(login).matches();
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
        Login login = (Login) obj;
        return value.equals(login.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
} 