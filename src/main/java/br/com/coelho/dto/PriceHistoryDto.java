package br.com.coelho.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceHistoryDto implements Serializable {
    private UUID id;
    private UUID productId;
    private UUID supermarketId;
    private double price;
    private LocalDateTime date;
}