package br.com.coelho.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartItemListResponse {
    private List<CartItemResponse> cartItems;
    private int totalCartItems;
    private int totalProducts;
    private String amountItems;
    private String subtotalChecked;
}
