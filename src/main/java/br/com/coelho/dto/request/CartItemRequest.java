package br.com.coelho.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private UUID id;
    private String unitValue;
    private int amountOfProduct;
    @JsonProperty("isChecked")
    private boolean isChecked;
}
