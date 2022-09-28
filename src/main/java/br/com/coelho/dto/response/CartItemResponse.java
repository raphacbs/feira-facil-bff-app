package br.com.coelho.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CartItemResponse implements Serializable {
    private String id;
    private String shoppingCartId;
    private ProductResponse product;
    private String price;
    private int amountOfProduct;
    private String subtotal;
    private String createdAt;
    @JsonProperty("isChecked")
    private boolean isChecked;
}

