package br.com.coelho.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse implements Serializable {
    private  String id;
    private  String description;
    private  String brand;
    private  String image;
    private  String ean;
    private  String createAt;
    private  String updateAt;
}