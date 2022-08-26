package br.com.coelho.service;

import br.com.coelho.dto.ProductDto;
import br.com.coelho.dto.request.ProductRequest;
import br.com.coelho.helper.AuthHelper;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Builder
public class SearchProductByDescription implements  SearchProduct{
    private static final Logger logger = LoggerFactory.getLogger(SearchProductByDescription.class);
    @Override
    public List<ProductDto> get(ProductRequest productRequest) {
        logger.info("Searching product by description: {}", productRequest.getDescription());
        HttpEntity<Void> requestEntity = new HttpEntity<>(AuthHelper.getHeaderAuth());
        final ResponseEntity<ProductDto[]> responseEntity = restTemplate.exchange(BASE_URL + "/api/v1/products?description=" + productRequest.getDescription(),
                HttpMethod.GET,
                requestEntity,
                ProductDto[].class);
        if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.NO_CONTENT) {
            logger.error("Error getByEna in backend: {} ", (responseEntity.hasBody() ? responseEntity.getBody() : responseEntity.getStatusCodeValue()));
            return new ArrayList<ProductDto>();
        }
        ProductDto[] productDtos = responseEntity.getBody();
        assert productDtos != null;
        return Arrays.asList(productDtos);
    }
}
