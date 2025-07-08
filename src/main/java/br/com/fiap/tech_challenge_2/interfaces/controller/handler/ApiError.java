package br.com.fiap.tech_challenge_2.interfaces.controller.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ApiError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;
    private List<String> errors = new ArrayList<>();

    public ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(int status, String message, String path) {
        this();
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public void addError(String error) {
        errors.add(error);
    }
}