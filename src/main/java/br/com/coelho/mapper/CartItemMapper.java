package br.com.coelho.mapper;

import br.com.coelho.dto.CartItemDto;
import br.com.coelho.dto.request.CartItemRequest;
import br.com.coelho.dto.response.CartItemListResponse;
import br.com.coelho.dto.response.CartItemResponse;
import org.mapstruct.factory.Mappers;

import java.text.ParseException;
import java.util.List;

public interface CartItemMapper {
    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);
    List<CartItemResponse> transform(List<CartItemDto> shoppingCartProducts);
    CartItemListResponse toCartItemListResponse(List<CartItemDto> shoppingCartProducts);
    CartItemResponse transform(CartItemDto shoppingCartProducts);
    CartItemDto transform(CartItemRequest cartItemRequest) throws ParseException;
}
