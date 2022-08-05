package br.com.coelho.mapper;

import br.com.coelho.dto.ShoppingCartDto;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface ShoppingCartMapper {
    ShoppingCartMapper INSTANCE = Mappers.getMapper(ShoppingCartMapper.class);

    ShoppingCartDto transform(ShoppingCartDto shoppingCartDto);
    List<ShoppingCartDto> transform(List<ShoppingCartDto>  shoppingCartDtos);
}
