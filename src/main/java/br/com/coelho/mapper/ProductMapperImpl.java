package br.com.coelho.mapper;

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
                .manufacturer(productDto.getManufacturer())
                .image(productDto.getImage())
                .updateAt(productDto.getUpdateAt() == null ? null : formatDate(productDto.getUpdateAt().toString()))
                .id(productDto.getId().toString())
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
                .manufacturer(productRequest.getManufacturer())
                .createAt(LocalDateTime.now())
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
