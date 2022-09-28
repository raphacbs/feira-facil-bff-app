package br.com.coelho.mapper;

import br.com.coelho.dto.ProductDto;
import br.com.coelho.dto.CartItemDto;
import br.com.coelho.dto.request.CartItemRequest;
import br.com.coelho.dto.response.CartItemListResponse;
import br.com.coelho.dto.response.CartItemResponse;
import br.com.coelho.dto.response.CartItemResponsePage;
import br.com.coelho.dto.response.CartItemResponsePageInfo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
        List<Double> valuesChecked = new ArrayList<>();
        cartItemResponseList.forEach(item -> {
            try {
                values.add(parseToDouble(item.getSubtotal()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        cartItemResponseList.forEach(item -> {
            try {
                if(item.isChecked()){
                    valuesChecked.add(parseToDouble(item.getSubtotal()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        double subtotal  = values.stream().mapToDouble(Double::doubleValue).sum();
        double subtotalChecked  = valuesChecked.stream().mapToDouble(Double::doubleValue).sum();
        return CartItemListResponse.builder()
                .cartItems(cartItemResponseList)
                .amountItems(parseCurrency(subtotal))
                .totalCartItems(cartItemResponseList.size())
                .totalProducts(cartItemResponseList.stream().mapToInt(CartItemResponse::getAmountOfProduct).sum())
                .totalProductsChecked(cartItemResponseList.stream().mapToInt(item-> item.isChecked()? item.getAmountOfProduct(): 0).sum())
                .subtotalChecked(parseCurrency(subtotalChecked))
                .build();

    }

    @Override
    public CartItemResponsePageInfo toCartItemListResponse(CartItemResponsePage cartItemResponsePage) {
        //CartItemListResponse cartItemListResponse = toCartItemListResponse(cartItemResponsePage.getContent());
        List<CartItemResponse> cartItemResponseList = transform(cartItemResponsePage.getCartItems());
        CartItemListResponse cartItemListResponse = CartItemListResponse.builder()
                .cartItems(cartItemResponseList)
                .subtotalChecked(parseCurrency(cartItemResponsePage.getSubtotalChecked()))
                .amountItems(parseCurrency(cartItemResponsePage.getAmountItems()))
                .totalCartItems(cartItemResponsePage.getTotalCartItems())
                .totalProducts(cartItemResponsePage.getTotalProducts())
                .totalProductsChecked(cartItemResponsePage.getTotalProductsChecked())
                .build();
        return CartItemResponsePageInfo.builder()
                .content(cartItemListResponse)
                .last(cartItemResponsePage.isLast())
                .pageNo(cartItemResponsePage.getPageNo())
                .pageSize(cartItemResponsePage.getPageSize())
                .totalElements(cartItemResponsePage.getTotalElements())
                .totalPages(cartItemResponsePage.getTotalPages())
                .build();

    }

    @Override
    public void updateCartItemResonsePageInfo(CartItemDto cartItemDto, CartItemResponsePageInfo cartItemResponsePageInfo) {
        final CartItemResponse cartItemResponse = transform(cartItemDto);
        cartItemResponsePageInfo.getContent().setCartItems(Collections.singletonList(cartItemResponse));
    }



    @Override
    public CartItemResponse transform(CartItemDto cartItemDto) {

        return CartItemResponse.builder()
                .shoppingCartId(cartItemDto.getShoppingCart().getId().toString())
                .amountOfProduct(cartItemDto.getAmountOfProduct())
                .subtotal(parseCurrency(cartItemDto.getSubtotal()))
                .product(ProductMapper.INSTANCE.transfome(cartItemDto.getProduct()))
                .id(cartItemDto.getId().toString())
                .price(parseCurrency(cartItemDto.getUnitValue()))
                .amountOfProduct(cartItemDto.getAmountOfProduct())
                .subtotal(parseCurrency(cartItemDto.getSubtotal()))
                .createdAt(cartItemDto.getCreatedAt() != null ? formatDate(cartItemDto.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)) : "")
                .isChecked(cartItemDto.isChecked())
                .build();
    }

    @Override
    public CartItemDto transform(CartItemRequest cartItemRequest) throws ParseException {
        return CartItemDto.builder()
                .shoppingCart(null)
                .amountOfProduct(cartItemRequest.getAmountOfProduct())
                .unitValue(parseToDouble(cartItemRequest.getPrice()))
                .isChecked(cartItemRequest.isChecked())
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
