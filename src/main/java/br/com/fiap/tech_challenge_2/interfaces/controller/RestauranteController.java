package br.com.fiap.tech_challenge_2.interfaces.controller;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.ApiResponse;
import br.com.fiap.tech_challenge_2.application.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurantes")
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "Operações para gerenciamento de restaurantes")
public class RestauranteController {

    private final RestauranteService service;

    @Operation(summary = "Cria um novo restaurante")
    @PostMapping
    public ResponseEntity<ApiResponse<RestauranteDTO>> create(@Valid @RequestBody RestauranteDTO dto) {
        RestauranteDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created, "Restaurante criado com sucesso"));
    }

    @Operation(summary = "Busca um restaurante por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestauranteDTO>> findById(@PathVariable Long id) {
        RestauranteDTO dto = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    @Operation(summary = "Lista todos os restaurantes")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RestauranteDTO>>> findAll() {
        List<RestauranteDTO> dtos = service.findAll();
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @Operation(summary = "Atualiza um restaurante")
    @PutMapping("/id")
    public ResponseEntity<ApiResponse<RestauranteDTO>> update(@PathVariable Long id, @Valid @RequestBody RestauranteDTO dto) {
        var updated = service.update(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Restaurante atualizado com sucesso"));
    }

    @Operation(summary = "Remove um restaurante")
    @DeleteMapping("/id")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Restaurante removido com sucesso"));
    }
}