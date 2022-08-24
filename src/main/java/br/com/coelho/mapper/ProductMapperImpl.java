package br.com.coelho.mapper;

import br.com.coelho.dto.ProductCosmoDto;
import br.com.coelho.dto.ProductDto;
import br.com.coelho.request.ProductRequest;
import br.com.coelho.response.ProductResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProductMapperImpl implements ProductMapper {


    @Override
    public ProductResponse transfome(ProductDto productDto) {
        if(productDto == null){
            return null;
        }
        return ProductResponse.builder()
                .createAt(productDto.getCreateAt() == null ? null : formatDate(productDto.getCreateAt().toString()))
                .description(productDto.getDescription())
                .ean(productDto.getEan())
                .brand(productDto.getBrand())
                .image(productDto.getImage())
                .updateAt(productDto.getUpdateAt() == null ? null : formatDate(productDto.getUpdateAt().toString()))
                .id(productDto.getId().toString())
                .build();
    }

    @Override
    public ProductRequest transfome(ProductCosmoDto productCosmoDto) {
        if(productCosmoDto == null ){
            return null;
        }

        return ProductRequest.builder()
                .ean(productCosmoDto.getGtin())
                .image("https://drive.google.com/uc?id=1w361FjVApKKJn6g8H5NVZ3IVbL-fSpo4")
                .brand(productCosmoDto.getBrand() != null ? productCosmoDto.getBrand().getName() : "")
                .description(productCosmoDto.getDescription())
                .build();

    }

    @Override
    public ProductDto transfome(ProductRequest productRequest) {
        if(productRequest == null){
            return null;
        }
        return ProductDto.builder()
                .image(productRequest.getImage())
                .ean(productRequest.getEan())
                .description(productRequest.getDescription())
                .brand(productRequest.getBrand())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .id(productRequest.getId())
                .build();
    }

    @Override
    public List<ProductResponse> transforme(List<ProductDto> productDtoList) {
        List<ProductResponse> productResponseList = new ArrayList<ProductResponse>();
        productDtoList.forEach(productDto -> {
            productResponseList.add(transfome(productDto));
        });
        return productResponseList;
    }

    private String formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(date.replaceAll("\\.\\d+", ""), formatter).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
