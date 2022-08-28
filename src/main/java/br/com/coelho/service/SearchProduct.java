package br.com.coelho.service;

import br.com.coelho.dto.ProductDto;
import br.com.coelho.dto.request.ProductRequest;
import br.com.coelho.mapper.ProductMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public interface SearchProduct {
    ProductMapper productMapper = ProductMapper.INSTANCE;
    RestTemplate restTemplate = new RestTemplate();
    final String BASE_URL = System.getenv("BASE_URL");
    List<ProductDto> get(ProductRequest productRequest);
}
