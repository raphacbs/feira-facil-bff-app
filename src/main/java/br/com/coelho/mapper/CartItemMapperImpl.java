package br.com.coelho.mapper;

import br.com.coelho.dto.ProductDto;
import br.com.coelho.dto.CartItemDto;
import br.com.coelho.request.CartItemRequest;
import br.com.coelho.response.CartItemListResponse;
import br.com.coelho.response.CartItemResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CartItemMapperImpl implements CartItemMapper {
    @Override
    public List<CartItemResponse> transform(List<CartItemDto> shoppingCartProducts) {
        List<CartItemResponse> cartItemResponseList = new ArrayList<CartItemResponse>();
        shoppingCartProducts.forEach(cartItemDto -> {
            cartItemResponseList.add(transform(cartItemDto));
        });
        return cartItemResponseList;
    }

    @Override
    public CartItemListResponse toCartItemListResponse(List<CartItemDto> shoppingCartProducts) {
        List<CartItemResponse> cartItemResponseList = transform(shoppingCartProducts);
        cartItemResponseList =  cartItemResponseList.stream().sorted(Comparator.comparing(CartItemResponse::getId)).
                collect(Collectors.toList());
        List<Double> values = new ArrayList<>();
        cartItemResponseList.forEach(item -> {
            try {
                values.add(parseToDouble(item.getSubtotal()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        double subtotal  = values.stream().mapToDouble(Double::doubleValue).sum();
        return CartItemListResponse.builder()
                .cartItems(cartItemResponseList)
                .amountItems(parseCurrency(subtotal))
                .totalCartItems(cartItemResponseList.size())
                .totalProducts(cartItemResponseList.stream().mapToInt(CartItemResponse::getAmountOfProduct).sum())
                .build();

    }

    @Override
    public CartItemResponse transform(CartItemDto shoppingCartProducts) {
        return CartItemResponse.builder()
                .shoppingCartId(shoppingCartProducts.getShoppingCart().getId().toString())
                .amountOfProduct(shoppingCartProducts.getAmountOfProduct())
                .subtotal(parseCurrency(shoppingCartProducts.getSubtotal()))
                .product(ProductMapper.INSTANCE.transfome(shoppingCartProducts.getProduct()))
                .id(shoppingCartProducts.getId().toString())
                .unitValue(parseCurrency(shoppingCartProducts.getUnitValue()))
                .amountOfProduct(shoppingCartProducts.getAmountOfProduct())
                .subtotal(parseCurrency(shoppingCartProducts.getSubtotal()))
                .build();
    }

    @Override
    public CartItemDto transform(CartItemRequest cartItemRequest) throws ParseException {
        return CartItemDto.builder()
                .shoppingCart(null)
                .amountOfProduct(cartItemRequest.getAmountOfProduct())
                .unitValue(parseToDouble(cartItemRequest.getUnitValue()))
                .product(ProductDto.builder().id(cartItemRequest.getProductId()).build())
                .id(cartItemRequest.getId())
                .build();
    }

    private String parseCurrency(final double amount) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("#,##0.00");
        return df.format(amount);
    }

    private String formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(date.replaceAll("\\.\\d+", ""), formatter).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private double parseToDouble(String value) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number number = format.parse(value);
        return number.doubleValue();
    }
}
