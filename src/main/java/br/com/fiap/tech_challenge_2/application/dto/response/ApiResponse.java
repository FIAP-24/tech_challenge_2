package br.com.fiap.tech_challenge_2.application.dto.response;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        String status,
        T data,
        String message,
        LocalDateTime timestamp
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", data, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>("SUCCESS", data, message, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("ERROR", null, message, LocalDateTime.now());
    }
}