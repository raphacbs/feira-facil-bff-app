package br.com.coelho.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
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