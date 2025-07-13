package br.com.fiap.tech_challenge_2.interfaces.controller;

import br.com.fiap.tech_challenge_2.application.dto.request.RestauranteRequestDTO;
import br.com.fiap.tech_challenge_2.application.service.RestauranteService;
import br.com.fiap.tech_challenge_2.domain.model.Restaurante;
import br.com.fiap.tech_challenge_2.application.dto.response.ApiResponse;
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

    private final RestauranteService restauranteService;

    @Operation(summary = "Cria um novo restaurante")
    @PostMapping
    public ResponseEntity<Restaurante> create(@Valid @RequestBody RestauranteRequestDTO dto) {
        Restaurante created = restauranteService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Busca um restaurante por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Restaurante> findById(@PathVariable Long id) {
        Restaurante restaurante = restauranteService.findById(id);
        return ResponseEntity.ok(restaurante);
    }

    @Operation(summary = "Lista todos os restaurantes")
    @GetMapping
    public ResponseEntity<List<Restaurante>> findAll() {
        List<Restaurante> restaurantes = restauranteService.findAll();
        return ResponseEntity.ok(restaurantes);
    }

    @Operation(summary = "Atualiza um restaurante existente")
    @PutMapping("/{id}")
    public ResponseEntity<Restaurante> update(@PathVariable Long id, @Valid @RequestBody RestauranteRequestDTO dto) {
        Restaurante updated = restauranteService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Remove um restaurante")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restauranteService.delete(id);
        return ResponseEntity.ok().build();
    }
}