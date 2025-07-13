package br.com.fiap.tech_challenge_2.domain.event;

import br.com.fiap.tech_challenge_2.domain.model.Usuario;

import java.time.LocalDateTime;

public class UsuarioCreatedEvent {
    
    private final Usuario usuario;
    private final LocalDateTime createdAt;
    
    public UsuarioCreatedEvent(Usuario usuario) {
        this.usuario = usuario;
        this.createdAt = LocalDateTime.now();
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
} 