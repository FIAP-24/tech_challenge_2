package br.com.fiap.tech_challenge_2.application.mapper;

import br.com.fiap.tech_challenge_2.application.dto.request.ItemCardapioRequestDTO;
import br.com.fiap.tech_challenge_2.application.dto.response.ItemCardapioResponse;
import br.com.fiap.tech_challenge_2.domain.model.ItemCardapio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemCardapioMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    @Mapping(target = "descricao", source = "descricao")
    @Mapping(target = "preco", source = "preco")
    @Mapping(target = "disponivelApenasNoLocal", source = "disponivelApenasNoLocal")
    @Mapping(target = "fotoPath", source = "fotoPath")
    @Mapping(target = "precoFormatado", expression = "java(itemCardapio.getFormattedPrice())")
    ItemCardapioResponse toResponse(ItemCardapio itemCardapio);

    List<ItemCardapioResponse> toResponseList(List<ItemCardapio> itemCardapios);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurante", ignore = true)
    ItemCardapio toEntity(ItemCardapioRequestDTO request);
} 