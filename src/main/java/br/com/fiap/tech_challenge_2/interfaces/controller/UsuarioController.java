package br.com.fiap.tech_challenge_2.interfaces.controller;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioLoginRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioPassRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.ApiResponse;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usurios", description = "Operaçōes relacionadas a usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;


    @Operation(summary = "Lista todos os usuários.")
    @GetMapping
    public ResponseEntity<Set<UsuarioResponse>> findAll() {
        Set<UsuarioResponse> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Busca usuário por ID.")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Cria um novo usuário.")
    @PostMapping
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse created = usuarioService.save(usuarioRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @Operation(summary = "Atualiza usuário existente por ID.")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioEditRequest usuarioEditRequest) {
        UsuarioResponse updated = usuarioService.update(id, usuarioEditRequest);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Autentica usuário.")
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@Valid @RequestBody UsuarioLoginRequest loginRequest) {
        boolean authenticated = usuarioService.authenticate(loginRequest);
        return ResponseEntity.ok(authenticated);
    }

    @Operation(summary = "Remove usuário por ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Troca senha do usuário.")
    @PutMapping("/password/{id}")
    public ResponseEntity<String> updatePass(@PathVariable Long id,@Valid @RequestBody UsuarioPassRequest usuarioPassRequest) {
        usuarioService.alterPassword(id, usuarioPassRequest);
        return ResponseEntity.ok("Senha alterada com sucesso.");
    }
}