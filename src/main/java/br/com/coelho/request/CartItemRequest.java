package br.com.coelho.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {
    private UUID productId;
    private double unitValue;
    private int amountOfProduct;
}
