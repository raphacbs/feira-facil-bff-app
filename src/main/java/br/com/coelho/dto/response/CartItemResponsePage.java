package br.com.coelho.dto.response;

import br.com.coelho.dto.CartItemDto;
import lombok.Data;

import java.util.List;

@Data
public class CartItemResponsePage extends ResponsePage<CartItemDto> {
    private List<CartItemDto> cartItems;
    private int totalCartItems;
    private int totalProducts;
    private int totalProductsChecked;
    private double amountItems;
    private double subtotalChecked;

}
