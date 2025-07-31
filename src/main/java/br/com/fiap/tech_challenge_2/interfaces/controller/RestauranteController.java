package br.com.fiap.tech_challenge_2.interfaces.controller;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.ApiResponse;
import br.com.fiap.tech_challenge_2.application.dto.response.RestauranteResponse;
import br.com.fiap.tech_challenge_2.application.service.RestauranteService;
import br.com.fiap.tech_challenge_2.application.usecase.CreateRestauranteUseCase;
import br.com.fiap.tech_challenge_2.application.usecase.DeleteRestauranteUseCase;
import br.com.fiap.tech_challenge_2.application.usecase.FindRestauranteUseCase;
import br.com.fiap.tech_challenge_2.application.usecase.UpdateRestauranteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/restaurantes")
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "Operações para gerenciamento de restaurantes")
public class RestauranteController {

    private final CreateRestauranteUseCase createRestauranteUseCase;
    private final FindRestauranteUseCase findRestauranteUseCase;
    private final UpdateRestauranteUseCase updateRestauranteUseCase;
    private final DeleteRestauranteUseCase deleteRestauranteUseCase;

    @Operation(summary = "Cria um novo restaurante")
    @PostMapping
    public ResponseEntity<ApiResponse<RestauranteResponse>> create(@Valid @RequestBody RestauranteRequest restauranteRequest) {
        RestauranteResponse created = createRestauranteUseCase.execute(restauranteRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Restaurante criado com sucesso"));
    }

    @Operation(summary = "Busca um restaurante por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestauranteResponse>> findById(@PathVariable Long id) {
        RestauranteResponse restaurante = findRestauranteUseCase.findById(id);
        return ResponseEntity.ok(ApiResponse.success(restaurante));
    }

    @Operation(summary = "Lista todos os restaurantes")
    @GetMapping
    public ResponseEntity<ApiResponse<Set<RestauranteResponse>>> findAll() {
        Set<RestauranteResponse> restaurantes = findRestauranteUseCase.findAll();
        return ResponseEntity.ok(ApiResponse.success(restaurantes));
    }

    @Operation(summary = "Atualiza um restaurante")
    @PutMapping("/id")
    public ResponseEntity<ApiResponse<RestauranteResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody RestauranteRequest restauranteRequest
    ) {
        RestauranteResponse updated = updateRestauranteUseCase.execute(id, restauranteRequest);
        return ResponseEntity.ok(ApiResponse.success(updated, "Restaurante atualizado com sucesso"));
    }

    @Operation(summary = "Remove um restaurante")
    @DeleteMapping("/id")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        deleteRestauranteUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Restaurante removido com sucesso"));
    }
}