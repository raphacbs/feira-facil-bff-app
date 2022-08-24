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
public class ProductRequest {
    private String description;
    private String brand;
    private String image;
    private String ean;
    private UUID id;

}
