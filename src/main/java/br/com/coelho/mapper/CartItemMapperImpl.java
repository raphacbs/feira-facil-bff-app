package br.com.coelho.mapper;

import br.com.coelho.dto.ProductDto;
import br.com.coelho.dto.CartItemDto;
import br.com.coelho.request.CartItemRequest;
import br.com.coelho.response.ShoppingCardProductsResponse;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CartItemMapperImpl implements CartItemMapper {
    @Override
    public List<ShoppingCardProductsResponse> transform(List<CartItemDto> shoppingCartProducts) {
        List<ShoppingCardProductsResponse> shoppingCardProductsResponseList = new ArrayList<ShoppingCardProductsResponse>();
        shoppingCartProducts.forEach(cartItemDto -> {
            shoppingCardProductsResponseList.add(transform(cartItemDto));
        });
        return shoppingCardProductsResponseList;
    }

    @Override
    public ShoppingCardProductsResponse transform(CartItemDto shoppingCartProducts) {
        return ShoppingCardProductsResponse.builder()
                .shoppingCartId(shoppingCartProducts.getShoppingCart().getId().toString())
                .amountOfProduct(shoppingCartProducts.getAmountOfProduct())
                .subtotal(parseCurrency(shoppingCartProducts.getSubtotal()))
                .product(ProductMapper.INSTANCE.transfome(shoppingCartProducts.getProduct()))
                .shoppingCartProductId(shoppingCartProducts.getId().toString())
                .unitValue(parseCurrency(shoppingCartProducts.getUnitValue()))
                .amountOfProduct(shoppingCartProducts.getAmountOfProduct())
                .subtotal(parseCurrency(shoppingCartProducts.getSubtotal()))
                .build();
    }

    @Override
    public CartItemDto transform(CartItemRequest cartItemRequest) {
        return CartItemDto.builder()
                .shoppingCart(null)
                .amountOfProduct(cartItemRequest.getAmountOfProduct())
                .unitValue(cartItemRequest.getUnitValue())
                .product(ProductDto.builder().id(cartItemRequest.getProductId()).build())
                .build();
    }

    private String parseCurrency(final double amount) {
        final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(amount);
    }

    private String formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(date.replaceAll("\\.\\d+", ""), formatter).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
