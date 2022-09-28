package br.com.coelho.mapper;

import br.com.coelho.dto.ShoppingCartDto;
import br.com.coelho.dto.response.ShoppingListResponsePage;
import br.com.coelho.dto.response.ShoppingListResponse;
import br.com.coelho.dto.response.ShoppingListResponsePageInfo;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface ShoppingListMapper {
    ShoppingListMapper INSTANCE = Mappers.getMapper(ShoppingListMapper.class);

    ShoppingListResponse transform(ShoppingCartDto shoppingCartDto);
    List<ShoppingListResponse> transform(List<ShoppingCartDto>  shoppingCartDtos);
    ShoppingListResponsePageInfo transform(ShoppingListResponsePage shoppingListResponsePage);

}
