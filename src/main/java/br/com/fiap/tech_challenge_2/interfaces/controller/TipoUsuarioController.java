package br.com.fiap.tech_challenge_2.interfaces.controller;

import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.ApiResponse;
import br.com.fiap.tech_challenge_2.application.usecase.CreateTipoUsuarioUseCase;
import br.com.fiap.tech_challenge_2.application.usecase.DeleteTipoUsuarioUseCase;
import br.com.fiap.tech_challenge_2.application.usecase.FindTipoUsuarioUseCase;
import br.com.fiap.tech_challenge_2.application.usecase.UpdateTipoUsuarioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-usuario")
@RequiredArgsConstructor
@Tag(name = "Tipos de Usuário", description = "Operações relacionadas a tipos de usuário")
public class TipoUsuarioController {

    private final CreateTipoUsuarioUseCase createTipoUsuarioUseCase;
    private final FindTipoUsuarioUseCase findTipoUsuarioUseCase;
    private final UpdateTipoUsuarioUseCase updateTipoUsuarioUseCase;
    private final DeleteTipoUsuarioUseCase deleteTipoUsuarioUseCase;

    @Operation(summary = "Cria um novo tipo de usuário")
    @PostMapping
    public ResponseEntity<ApiResponse<TipoUsuarioDTO>> create(@Valid @RequestBody TipoUsuarioDTO dto) {
        TipoUsuarioDTO created = createTipoUsuarioUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created, "Tipo de usuário criado com sucesso"));
    }

    @Operation(summary = "Busca um tipo de usuário por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoUsuarioDTO>> findById(@PathVariable Long id) {
        TipoUsuarioDTO dto = findTipoUsuarioUseCase.findById(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @Operation(summary = "Lista todos os tipos de usuário")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TipoUsuarioDTO>>> findAll() {
        List<TipoUsuarioDTO> dtos = findTipoUsuarioUseCase.findAll();
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @Operation(summary = "Atualiza um tipo de usuário existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoUsuarioDTO>> update(@PathVariable Long id, @Valid @RequestBody TipoUsuarioDTO dto) {
        TipoUsuarioDTO updated = updateTipoUsuarioUseCase.execute(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Tipo de usuário atualizado com sucesso"));
    }

    @Operation(summary = "Remove um tipo de usuário")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        deleteTipoUsuarioUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Tipo de usuário removido com sucesso"));
    }
}