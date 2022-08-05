package br.com.coelho.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartProductsDto implements Serializable {
    private  UUID id;
    private  ShoppingCartDto shoppingCart;
    private  ProductDto product;
    private double unitValue;
    private int amountOfProduct;
    private double subtotal;
}
