package br.com.fiap.tech_challenge_2.interfaces.controller;

import br.com.fiap.tech_challenge_2.application.dto.request.ItemCardapioRequestDTO;
import br.com.fiap.tech_challenge_2.application.dto.request.UsuarioEditRequest;
import br.com.fiap.tech_challenge_2.application.dto.response.ApiResponse;
import br.com.fiap.tech_challenge_2.application.dto.response.UsuarioResponse;
import br.com.fiap.tech_challenge_2.application.usecase.CreateItemCardapioUseCase;
import br.com.fiap.tech_challenge_2.application.usecase.DeleteItemCardapioUseCase;
import br.com.fiap.tech_challenge_2.application.usecase.FindItemCardapioUseCase;
import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
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
    private final DeleteItemCardapioUseCase deleteItemCardapioUseCase;

    @Operation(summary = "Lista todos os itens do cardápio.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemCardapio>>> findAll() {
        List<ItemCardapio> items = findItemCardapioUseCase.findAll();
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @Operation(summary = "Busca item do cardápio por ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemCardapio>> findById(@PathVariable Long id) {
        ItemCardapio item = findItemCardapioUseCase.findById(id);
        return ResponseEntity.ok(ApiResponse.success(item));
    }

    @Operation(summary = "Lista itens do cardápio por restaurante.")
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<ApiResponse<List<ItemCardapio>>> findByRestauranteId(@PathVariable Long restauranteId) {
        List<ItemCardapio> items = findItemCardapioUseCase.findByRestauranteId(restauranteId);
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @Operation(summary = "Lista itens disponíveis do cardápio por restaurante.")
    @GetMapping("/restaurante/{restauranteId}/disponiveis")
    public ResponseEntity<ApiResponse<List<ItemCardapio>>> findAvailableByRestauranteId(@PathVariable Long restauranteId) {
        List<ItemCardapio> items = findItemCardapioUseCase.findAvailableByRestauranteId(restauranteId);
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @Operation(summary = "Cria um novo item do cardápio.")
    @PostMapping
    public ResponseEntity<ApiResponse<ItemCardapio>> create(@Valid @RequestBody ItemCardapioRequestDTO request) {
        ItemCardapio created = createItemCardapioUseCase.execute(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Item do cardápio criado com sucesso"));
    }

    @Operation(summary = "Deleta um item do cardápio por ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        deleteItemCardapioUseCase.execute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Item do cardápio removido com sucesso"));
    }

    @Operation(summary = "Atualiza um item do cardápio")
    @PutMapping
    public ResponseEntity<ApiResponse<UsuarioResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioEditRequest usuarioEditRequest) {
        ItemCardapioResponse updated = itemCardapioUseCase.execute(id, usuarioEditRequest);
        return ResponseEntity.ok(ApiResponse.success(updated, "Usuário atualizado com sucesso"));
    }
} 