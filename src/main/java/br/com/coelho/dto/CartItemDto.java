package br.com.coelho.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto implements Serializable {
    private  UUID id;
    private  ShoppingCartDto shoppingCart;
    private  ProductDto product;
    private double unitValue;
    private int amountOfProduct;
    private double subtotal;
    private boolean isChecked;
    private LocalDateTime createdAt;
}
