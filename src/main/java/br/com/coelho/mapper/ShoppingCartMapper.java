package br.com.coelho.mapper;

import br.com.coelho.dto.ShoppingCartDto;
import br.com.coelho.response.ShoppingCartResponse;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface ShoppingCartMapper {
    ShoppingCartMapper INSTANCE = Mappers.getMapper(ShoppingCartMapper.class);

    ShoppingCartResponse transform(ShoppingCartDto shoppingCartDto);
    List<ShoppingCartResponse> transform(List<ShoppingCartDto>  shoppingCartDtos);
}
