package br.com.coelho.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCosmoDto {
    private String description;
    private String gtin;
    private Brand brand;

    @Data
    public class Brand {
        private String name;
        private String picture;
    }

}
