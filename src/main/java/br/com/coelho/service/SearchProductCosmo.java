package br.com.coelho.service;

import br.com.coelho.dto.ProductCosmoDto;
import br.com.coelho.dto.ProductDto;
import br.com.coelho.dto.request.ProductRequest;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.List;
@Builder
public class SearchProductCosmo implements SearchProduct {

    private static final Logger logger = LoggerFactory.getLogger(SearchProductCosmo.class);

    @Override
    public List<ProductDto> get(ProductRequest productRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Cosmos-Token", System.getenv("X_COSMOS_TOKEN"));
        HttpEntity<Void> requestCosmo = new HttpEntity<>(headers);
        try {
            final ResponseEntity<ProductCosmoDto> productCosmoDtoResponseEntity = restTemplate.exchange(
                    "https://api.cosmos.bluesoft.com.br/gtins/" + productRequest.getEan(), HttpMethod.GET, requestCosmo, ProductCosmoDto.class);
            if (productCosmoDtoResponseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.info("Product with ean '{}' not found in Cosmo API", productRequest.getEan());
            }
            logger.info("Product with ean '{}' found in Cosmo API", productRequest.getEan());
            final ProductCosmoDto productCosmoDto = productCosmoDtoResponseEntity.getBody();
            final ProductRequest productRequest1 = productMapper.transfome(productCosmoDto);
            final ProductDto productDto = productMapper.transfome(productRequest1);
            final ArrayList<ProductDto> productDtos = new ArrayList<>();
            if(productDto != null)
                productDtos.add(productDto);
            return productDtos;
        } catch (HttpStatusCodeException exception) {
            logger.error("Error when searched in Cosomo API.");
            logger.error("Error: Code:{}, Message:{}", exception.getStatusCode().value(), exception.getMessage());
        }
        return new ArrayList<ProductDto>();
    }
}
