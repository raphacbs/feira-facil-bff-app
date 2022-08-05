package br.com.coelho.mapper;

import br.com.coelho.dto.ShoppingCartProductsDto;
import br.com.coelho.request.ShoppingCardProductsRequest;
import br.com.coelho.response.ShoppingCardProductsResponse;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface ShoppingCartProductsMapper {
    ShoppingCartProductsMapper INSTANCE = Mappers.getMapper(ShoppingCartProductsMapper.class);
    List<ShoppingCardProductsResponse> transform(List<ShoppingCartProductsDto> shoppingCartProducts);
    ShoppingCardProductsResponse transform(ShoppingCartProductsDto shoppingCartProducts);
    ShoppingCartProductsDto transform(ShoppingCardProductsRequest shoppingCardProductsRequest);
}
