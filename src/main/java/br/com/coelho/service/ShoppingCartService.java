package br.com.coelho.service;

import br.com.coelho.dto.ShoppingCartDto;
import br.com.coelho.dto.ShoppingCartProductsDto;
import br.com.coelho.mapper.ShoppingCartMapper;
import br.com.coelho.mapper.ShoppingCartProductsMapper;
import br.com.coelho.request.ShoppingCardProductsRequest;
import br.com.coelho.response.ShoppingCardProductsResponse;
import br.com.coelho.response.ShoppingCartResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper = ShoppingCartMapper.INSTANCE;
    private final ShoppingCartProductsMapper shoppingCartProductsMapper = ShoppingCartProductsMapper.INSTANCE;


    public List<ShoppingCartResponse> getAll() {
        RestTemplate restTemplate = new RestTemplate();
        ShoppingCartDto[] shoppingCartDtos = restTemplate
                .getForObject(System.getenv("BASE_URL") + "/api/v1/shopping-carts", ShoppingCartDto[].class);
        assert shoppingCartDtos != null;
        List<ShoppingCartDto> shoppingCartDtoList = Arrays.asList(shoppingCartDtos);
        return this.shoppingCartMapper.transform(shoppingCartDtoList);
    }

    public ResponseEntity create(ShoppingCartDto shoppingCartDto) {
        RestTemplate restTemplate = new RestTemplate();
        ShoppingCartDto shoppingCartDtoResponse = restTemplate.postForObject(System.getenv("BASE_URL") + "/api/v1/shopping-carts"
                , shoppingCartDto
                , ShoppingCartDto.class);
        final ShoppingCartResponse shoppingCartResponse = this.shoppingCartMapper.transform(shoppingCartDtoResponse);
        return ResponseEntity.ok().body(shoppingCartResponse);
    }

    public ResponseEntity<List<ShoppingCardProductsResponse>> getProducts(UUID shoppingCartId) {
        RestTemplate restTemplate = new RestTemplate();
        ShoppingCartProductsDto[] shoppingCartProductsDtos = restTemplate.getForObject(System.getenv("BASE_URL") + "/api/v1/shopping-cart-products?shoppingCartId=" + shoppingCartId
                , ShoppingCartProductsDto[].class);
        List<ShoppingCartProductsDto> shoppingCartProductsDtoList = Arrays.asList(shoppingCartProductsDtos);
        final List<ShoppingCardProductsResponse> shoppingCardProductsResponseList = this.shoppingCartProductsMapper.transform(shoppingCartProductsDtoList);
        return ResponseEntity.ok().body(shoppingCardProductsResponseList);
    }

    public ResponseEntity addProduct(UUID shoppingCartId, ShoppingCardProductsRequest shoppingCardProductsRequest) {
        RestTemplate restTemplate = new RestTemplate();
        final ShoppingCartProductsDto shoppingCartProductsDto = this.shoppingCartProductsMapper.transform(shoppingCardProductsRequest);
        shoppingCartProductsDto.setShoppingCart(ShoppingCartDto.builder().id(shoppingCartId).build());
        final ShoppingCartProductsDto shoppingCartProductsDtoSaved = restTemplate.postForObject(System.getenv("BASE_URL") + "/api/v1/shopping-carts/product",
                shoppingCartProductsDto,
                ShoppingCartProductsDto.class);
        final ShoppingCardProductsResponse shoppingCartResponse = this.shoppingCartProductsMapper.transform(shoppingCartProductsDtoSaved);
        return  ResponseEntity.ok().body(shoppingCartResponse);
    }
}
