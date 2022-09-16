package br.com.coelho.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CartItemResponse implements Serializable {
    private String id;
    private String shoppingCartId;
    private ProductResponse product;
    private String unitValue;
    private int amountOfProduct;
    private String subtotal;
    private String createdAt;
    private boolean isChecked;
}

