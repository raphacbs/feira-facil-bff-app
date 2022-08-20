package br.com.coelho.service;

import br.com.coelho.dto.ProductDto;
import br.com.coelho.mapper.ProductMapper;
import br.com.coelho.request.ProductRequest;
import br.com.coelho.response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    public Optional<ProductResponse> getByEan(String ean){
        RestTemplate restTemplate = new RestTemplate();
        final ProductDto productDto = restTemplate
                .getForObject(System.getenv("BASE_URL") + "/api/v1/products?ean=" + ean, ProductDto.class);
        final ProductResponse productResponse = productMapper.transfome(productDto);
        return Optional.ofNullable(productResponse);
    }

    public Optional<ProductResponse> create(ProductRequest productRequest) {
        final ProductDto productDto = this.productMapper.transfome(productRequest);
        RestTemplate restTemplate = new RestTemplate();
        final ProductDto productDtoSaved = restTemplate.postForObject(System.getenv("BASE_URL") + "/api/v1/products"
                , productDto
                , ProductDto.class);
        final ProductResponse productResponseSaved = this.productMapper.transfome(productDtoSaved);
        return Optional.ofNullable(productResponseSaved);

    }
}
