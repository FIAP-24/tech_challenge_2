package br.com.fiap.tech_challenge_2.interfaces.controller;

import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditPassRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioLoginRequest;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.ApiResponse;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.usecase.*;
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
@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
public class UsuarioController {

    private final CreateUsuarioUseCase createUsuarioUseCase;
    private final FindUsuarioUseCase findUsuarioUseCase;
    private final UpdateUsuarioUseCase updateUsuarioUseCase;
    private final DeleteUsuarioUseCase deleteUsuarioUseCase;
    private final AuthenticateUsuarioUseCase authenticateUsuarioUseCase;
    private final UpdatePassUsuarioUseCase passUsuarioUseCase;


    @Operation(summary = "Lista todos os usuários.")
    @GetMapping
    public ResponseEntity<ApiResponse<Set<UsuarioResponse>>> findAll() {
        Set<UsuarioResponse> usuarios = findUsuarioUseCase.findAll();
        return ResponseEntity.ok(ApiResponse.success(usuarios));
    }

    @Operation(summary = "Busca usuário por ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> findById(@PathVariable Long id) {
        UsuarioResponse usuario = findUsuarioUseCase.findById(id);
        return ResponseEntity.ok(ApiResponse.success(usuario));
    }

    @Operation(summary = "Cria um novo usuário.")
    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponse>> create(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse created = createUsuarioUseCase.execute(usuarioRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Usuário cadastrado com sucesso"));
    }

    @Operation(summary = "Atualiza usuário existente por ID.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioEditRequest usuarioEditRequest) {
        UsuarioResponse updated = updateUsuarioUseCase.execute(id, usuarioEditRequest);
        return ResponseEntity.ok(ApiResponse.success(updated, "Usuário atualizado com sucesso"));
    }

    @Operation(summary = "Autentica usuário.")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Boolean>> login(@Valid @RequestBody UsuarioLoginRequest loginRequest) {
        boolean authenticated = authenticateUsuarioUseCase.execute(loginRequest);
        return ResponseEntity.ok(
                authenticated ?
                        ApiResponse.success(true, "Autenticação bem-sucedida") :
                        ApiResponse.error("Falha na autenticação")
        );
    }

    @Operation(summary = "Remove usuário por ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        deleteUsuarioUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Usuário removido com sucesso"));
    }

    @Operation(summary = "Atualiza senha do usuário.")
    @PutMapping("/atualizar-senha")
    public ResponseEntity<ApiResponse<Boolean>> updatePass(@Valid @RequestBody UsuarioEditPassRequest passRequest) {
        boolean authenticated = passUsuarioUseCase.execute(passRequest);
        return ResponseEntity.ok(
                authenticated ?
                        ApiResponse.success(true, "Alteração bem-sucedida") :
                        ApiResponse.error("Falha na Alteração")
        );
    }
}