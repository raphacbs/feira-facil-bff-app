package br.com.coelho.mapper;

import br.com.coelho.dto.ProductDto;
import br.com.coelho.request.ProductRequest;
import br.com.coelho.response.ProductResponse;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductResponse transfome(ProductDto productDto);
    ProductDto transfome(ProductRequest productRequest);

    List<ProductResponse> transforme(List<ProductDto> productDtoList);
}
