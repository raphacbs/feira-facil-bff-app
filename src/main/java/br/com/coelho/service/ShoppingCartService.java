package br.com.coelho.service;

import br.com.coelho.dto.ShoppingCartDto;
import br.com.coelho.dto.CartItemDto;
import br.com.coelho.mapper.ShoppingCartMapper;
import br.com.coelho.mapper.CartItemMapper;
import br.com.coelho.request.CartItemRequest;
import br.com.coelho.response.CartItemListResponse;
import br.com.coelho.response.CartItemResponse;
import br.com.coelho.response.ShoppingCartResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper = ShoppingCartMapper.INSTANCE;
    private final CartItemMapper cartItemMapper = CartItemMapper.INSTANCE;


    public List<ShoppingCartResponse> getAll(boolean isArchived) {
        RestTemplate restTemplate = new RestTemplate();
        ShoppingCartDto[] shoppingCartDtos = restTemplate
                .getForObject(System.getenv("BASE_URL") + "/api/v1/shopping-carts?isArchived="+isArchived, ShoppingCartDto[].class);
        assert shoppingCartDtos != null;
        List<ShoppingCartDto> shoppingCartDtoList = Arrays.asList(shoppingCartDtos).stream().sorted(Comparator.comparing(ShoppingCartDto::getId)).
                collect(Collectors.toList());;
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

    public ResponseEntity<CartItemListResponse> getCartItems(UUID shoppingCartId) {
        RestTemplate restTemplate = new RestTemplate();
        CartItemDto[] cartItemDtos = restTemplate.getForObject(System.getenv("BASE_URL") + "/api/v1/cart-items?shoppingCartId=" + shoppingCartId
                , CartItemDto[].class);
        List<CartItemDto> cartItemDtoList = Arrays.asList(cartItemDtos);
        final CartItemListResponse cartItemListResponse = this.cartItemMapper.toCartItemListResponse(cartItemDtoList);
        return ResponseEntity.ok().body(cartItemListResponse);
    }

    public ResponseEntity addCartItem(UUID shoppingCartId, CartItemRequest cartItemRequest) throws ParseException {
        RestTemplate restTemplate = new RestTemplate();
        final CartItemDto cartItemDto = this.cartItemMapper.transform(cartItemRequest);
        cartItemDto.setShoppingCart(ShoppingCartDto.builder().id(shoppingCartId).build());
        final CartItemDto cartItemDtoSaved = restTemplate.postForObject(System.getenv("BASE_URL") + "/api/v1/shopping-carts/cart-item",
                cartItemDto,
                CartItemDto.class);
        final CartItemResponse shoppingCartResponse = this.cartItemMapper.transform(cartItemDtoSaved);
        return  ResponseEntity.ok().body(shoppingCartResponse);
    }

    public ResponseEntity updateCartItem(UUID shoppingCartId, CartItemRequest cartItemRequest) throws ParseException {
        RestTemplate restTemplate = new RestTemplate();
        final CartItemDto cartItemDto = this.cartItemMapper.transform(cartItemRequest);
        cartItemDto.setShoppingCart(ShoppingCartDto.builder().id(shoppingCartId).build());
        HttpEntity<CartItemDto> entity = new HttpEntity<CartItemDto>(cartItemDto);
        final ResponseEntity<CartItemDto> response = restTemplate.exchange(System.getenv("BASE_URL") + "/api/v1/shopping-carts/cart-item", HttpMethod.PUT, entity, CartItemDto.class);
        final CartItemResponse shoppingCartResponse = this.cartItemMapper.transform(response.getBody());
        return  ResponseEntity.ok().body(shoppingCartResponse);
    }

    public ResponseEntity update(ShoppingCartDto shoppingCartDto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ShoppingCartDto> entity = new HttpEntity<ShoppingCartDto>(shoppingCartDto);
        ResponseEntity<ShoppingCartDto> shoppingCartDtoResponse = restTemplate.exchange(System.getenv("BASE_URL") + "/api/v1/shopping-carts"
                ,HttpMethod.PUT
                , entity
                , ShoppingCartDto.class);
        final ShoppingCartResponse shoppingCartResponse = this.shoppingCartMapper.transform(shoppingCartDtoResponse.getBody());
        return ResponseEntity.ok().body(shoppingCartResponse);
    }

    public boolean deleteCartItem(UUID id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(System.getenv("BASE_URL") + "/api/v1/cart-items/" + id.toString());
        return true;
    }
}
