package br.com.coelho.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable {
    private  UUID id;
    private  String description;
    private  String brand;
    private  String image;
    private  String ean;
    private  LocalDateTime createAt = null;
    private  LocalDateTime updateAt = null;
    private PriceHistoryDto lastPrice;
}
