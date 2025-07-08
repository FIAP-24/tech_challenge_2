package br.com.fiap.tech_challenge_2.interfaces.controller;


import br.com.fiap.tech_challenge_2.application.dto.request.TipoUsuarioDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.ApiResponse;
import br.com.fiap.tech_challenge_2.application.service.TipoUsuarioService;
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

    private final TipoUsuarioService service;

    @Operation(summary = "Cria um novo tipo de usuário")
    @PostMapping
    public ResponseEntity<ApiResponse<TipoUsuarioDTO>> create(@Valid @RequestBody TipoUsuarioDTO dto) {
        TipoUsuarioDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created, "Tipo de usuário criado com sucesso"));
    }

    @Operation(summary = "Busca um tipo de usuário por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoUsuarioDTO>> findById(@PathVariable Long id) {
        TipoUsuarioDTO dto = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @Operation(summary = "Lista todos os tipos de usuário")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TipoUsuarioDTO>>> findAll() {
        List<TipoUsuarioDTO> dtos = service.findAll();
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @Operation(summary = "Atualiza um tipo de usuário existente")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoUsuarioDTO>> update(@PathVariable Long id, @Valid @RequestBody TipoUsuarioDTO dto) {
        TipoUsuarioDTO updated = service.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Tipo de usuário atualizado com sucesso"));
    }

    @Operation(summary = "Remove um tipo de usuário")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Tipo de usuário removido com sucesso"));
    }
}