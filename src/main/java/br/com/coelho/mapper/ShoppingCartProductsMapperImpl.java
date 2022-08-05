package br.com.coelho.mapper;

import br.com.coelho.dto.ProductDto;
import br.com.coelho.dto.ShoppingCartProductsDto;
import br.com.coelho.request.ShoppingCardProductsRequest;
import br.com.coelho.response.ShoppingCardProductsResponse;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartProductsMapperImpl implements ShoppingCartProductsMapper {
    @Override
    public List<ShoppingCardProductsResponse> transform(List<ShoppingCartProductsDto> shoppingCartProducts) {
        List<ShoppingCardProductsResponse> shoppingCardProductsResponseList = new ArrayList<ShoppingCardProductsResponse>();
        shoppingCartProducts.forEach(shoppingCartProductsDto -> {
            shoppingCardProductsResponseList.add(transform(shoppingCartProductsDto));
        });
        return shoppingCardProductsResponseList;
    }

    @Override
    public ShoppingCardProductsResponse transform(ShoppingCartProductsDto shoppingCartProducts) {
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
    public ShoppingCartProductsDto transform(ShoppingCardProductsRequest shoppingCardProductsRequest) {
        return ShoppingCartProductsDto.builder()
                .shoppingCart(null)
                .amountOfProduct(shoppingCardProductsRequest.getAmountOfProduct())
                .unitValue(shoppingCardProductsRequest.getUnitValue())
                .product(ProductDto.builder().id(shoppingCardProductsRequest.getProductId()).build())
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
