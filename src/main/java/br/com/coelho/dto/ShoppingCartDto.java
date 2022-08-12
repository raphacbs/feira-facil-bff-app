package br.com.coelho.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto implements Serializable {
    private UUID id;
    private String description;
    private String supermarket;
    private String createAt;
    private String updateAt;
    private double amount;
    private int amountProducts;
    private boolean isArchived;
}
