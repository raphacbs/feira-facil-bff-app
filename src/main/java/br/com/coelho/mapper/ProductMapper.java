package br.com.coelho.mapper;

import br.com.coelho.dto.ProductCosmoDto;
import br.com.coelho.dto.ProductDto;
import br.com.coelho.dto.request.ProductRequest;
import br.com.coelho.dto.response.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
    ProductResponse transfome(ProductDto productDto);
    ProductResponse transfome(List<ProductDto> productDto);
    ProductRequest transfome(ProductCosmoDto productCosmoDto);
    ProductDto transfome(ProductRequest productRequest);
    ProductListResponse transforme(List<ProductDto> productDtoList);
    ProductListResponse transfome(Optional<ProductResponse> productResponse);
    ProductResponsePageInfo transfome(ProductResponse productResponse);
    ProductResponsePageInfo transfome(ProductResponsePage productResponsePage);
}
