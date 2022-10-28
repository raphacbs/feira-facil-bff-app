package br.com.coelho.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@Data@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceHistoryResponse {
    private String id;
    private String productId;
    private String supermarketId;
    private String price;
    private String date;

}
