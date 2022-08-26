package br.com.coelho.mapper;

import br.com.coelho.dto.ProductDto;
import br.com.coelho.dto.CartItemDto;
import br.com.coelho.dto.request.CartItemRequest;
import br.com.coelho.dto.response.CartItemListResponse;
import br.com.coelho.dto.response.CartItemResponse;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    public CartItemResponse transform(CartItemDto cartItemDto) {

        return CartItemResponse.builder()
                .shoppingCartId(cartItemDto.getShoppingCart().getId().toString())
                .amountOfProduct(cartItemDto.getAmountOfProduct())
                .subtotal(parseCurrency(cartItemDto.getSubtotal()))
                .product(ProductMapper.INSTANCE.transfome(cartItemDto.getProduct()))
                .id(cartItemDto.getId().toString())
                .unitValue(parseCurrency(cartItemDto.getUnitValue()))
                .amountOfProduct(cartItemDto.getAmountOfProduct())
                .subtotal(parseCurrency(cartItemDto.getSubtotal()))
                .createdAt(cartItemDto.getCreatedAt() != null ? formatDate(cartItemDto.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)) : "")
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
                .createdAt(LocalDateTime.now())
                .build();
    }

    private String parseCurrency(final double amount) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("#,##0.00");
        return "R$ " + df.format(amount);
    }

    private String formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(date.replaceAll("\\.\\d+", ""), formatter).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    private double parseToDouble(String value) throws ParseException {
        value = value.replace("R$ ","");
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number number = format.parse(value);
        return number.doubleValue();
    }
}
