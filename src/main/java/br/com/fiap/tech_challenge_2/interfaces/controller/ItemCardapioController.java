package br.com.fiap.tech_challenge_2.interfaces.controller;

import br.com.fiap.tech_challenge_2.application.dto.request.ItemCardapioRequestDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.ApiResponse;
import br.com.fiap.tech_challenge_2.application.dto.response.ItemCardapioResponse;
import br.com.fiap.tech_challenge_2.application.usecase.CreateItemCardapioUseCase;
import br.com.fiap.tech_challenge_2.application.usecase.DeleteItemCardapioUseCase;
import br.com.fiap.tech_challenge_2.application.usecase.FindItemCardapioUseCase;
import br.com.fiap.tech_challenge_2.application.usecase.UpdateItemCardapioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/itens-cardapio")
@RequiredArgsConstructor
@Tag(name = "Itens do Cardápio", description = "Operações relacionadas a itens do cardápio")
public class ItemCardapioController {

    private final CreateItemCardapioUseCase createItemCardapioUseCase;
    private final FindItemCardapioUseCase findItemCardapioUseCase;
    private final UpdateItemCardapioUseCase updateItemCardapioUseCase;
    private final DeleteItemCardapioUseCase deleteItemCardapioUseCase;

    @Operation(summary = "Lista todos os itens do cardápio.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemCardapioResponse>>> findAll() {
        List<ItemCardapioResponse> items = findItemCardapioUseCase.findAll();
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @Operation(summary = "Busca item do cardápio por ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemCardapioResponse>> findById(@PathVariable Long id) {
        ItemCardapioResponse item = findItemCardapioUseCase.findById(id);
        return ResponseEntity.ok(ApiResponse.success(item));
    }

    @Operation(summary = "Lista itens do cardápio por restaurante.")
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<ApiResponse<List<ItemCardapioResponse>>> findByRestauranteId(@PathVariable Long restauranteId) {
        List<ItemCardapioResponse> items = findItemCardapioUseCase.findByRestauranteId(restauranteId);
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @Operation(summary = "Lista itens disponíveis do cardápio por restaurante.")
    @GetMapping("/restaurante/{restauranteId}/disponiveis")
    public ResponseEntity<ApiResponse<List<ItemCardapioResponse>>> findAvailableByRestauranteId(@PathVariable Long restauranteId) {
        List<ItemCardapioResponse> items = findItemCardapioUseCase.findAvailableByRestauranteId(restauranteId);
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @Operation(summary = "Cria um novo item do cardápio.")
    @PostMapping
    public ResponseEntity<ApiResponse<ItemCardapioResponse>> create(@Valid @RequestBody ItemCardapioRequestDTO request) {
        ItemCardapioResponse created = createItemCardapioUseCase.execute(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Item do cardápio criado com sucesso"));
    }

    @Operation(summary = "Atualiza um item do cardápio.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemCardapioResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ItemCardapioRequestDTO request
    ) {
        ItemCardapioResponse updated = updateItemCardapioUseCase.execute(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated, "Item do cardápio atualizado com sucesso"));
    }

    @Operation(summary = "Deleta um item do cardápio por ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        deleteItemCardapioUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Item do cardápio removido com sucesso"));
    }
}