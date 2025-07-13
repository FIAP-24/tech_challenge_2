package br.com.fiap.tech_challenge_2.interfaces.controller;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequestDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.ApiResponse;
import br.com.fiap.tech_challenge_2.application.usecase.CreateRestauranteUseCase;
import br.com.fiap.tech_challenge_2.application.usecase.FindRestauranteUseCase;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
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

    private final CreateRestauranteUseCase createRestauranteUseCase;
    private final FindRestauranteUseCase findRestauranteUseCase;

    @Operation(summary = "Cria um novo restaurante")
    @PostMapping
    public ResponseEntity<ApiResponse<Restaurante>> create(@Valid @RequestBody RestauranteRequestDTO dto) {
        Restaurante created = createRestauranteUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created, "Restaurante criado com sucesso"));
    }

    @Operation(summary = "Busca um restaurante por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Restaurante>> findById(@PathVariable Long id) {
        Restaurante restaurante = findRestauranteUseCase.findById(id);
        return ResponseEntity.ok(ApiResponse.success(restaurante));
    }

    @Operation(summary = "Lista todos os restaurantes")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Restaurante>>> findAll() {
        List<Restaurante> restaurantes = findRestauranteUseCase.findAll();
        return ResponseEntity.ok(ApiResponse.success(restaurantes));
    }
}