package br.com.coelho.service;

import br.com.coelho.dto.CartItemDto;
import br.com.coelho.dto.ShoppingCartDto;
import br.com.coelho.helper.AuthHelper;
import br.com.coelho.mapper.CartItemMapper;
import br.com.coelho.mapper.ShoppingCartMapper;
import br.com.coelho.request.CartItemRequest;
import br.com.coelho.response.CartItemListResponse;
import br.com.coelho.response.CartItemResponse;
import br.com.coelho.response.ShoppingCartResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper = ShoppingCartMapper.INSTANCE;
    private final CartItemMapper cartItemMapper = CartItemMapper.INSTANCE;
    private final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);


    public List<ShoppingCartResponse> getAll(boolean isArchived) {
        logger.info("Searching shopping lists archived: {}", isArchived);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Void> requestEntity = new HttpEntity<>(AuthHelper.getHeaderAuth());
        final ResponseEntity<ShoppingCartDto[]> exchange = restTemplate.exchange(System.getenv("BASE_URL") + "/api/v1/shopping-carts?isArchived=" + isArchived,
                HttpMethod.GET,
                requestEntity,
                ShoppingCartDto[].class);
        ShoppingCartDto[] shoppingCartDtos = exchange.getBody();
        assert shoppingCartDtos != null;
        List<ShoppingCartDto> shoppingCartDtoList = Arrays.stream(shoppingCartDtos).sorted(Comparator.comparing(ShoppingCartDto::getCreateAt).reversed()).
                collect(Collectors.toList());
        return this.shoppingCartMapper.transform(shoppingCartDtoList);
    }

    public ResponseEntity create(ShoppingCartDto shoppingCartDto) {
        shoppingCartDto.setCreateAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        shoppingCartDto.setUpdateAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        logger.info("Shopping Cart to create: {}", shoppingCartDto);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ShoppingCartDto> requestEntity = new HttpEntity<ShoppingCartDto>(shoppingCartDto, AuthHelper.getHeaderAuth());
        final ResponseEntity<ShoppingCartDto> exchange = restTemplate.exchange(System.getenv("BASE_URL") + "/api/v1/shopping-carts",
                HttpMethod.POST,
                requestEntity,
                ShoppingCartDto.class);
        ShoppingCartDto shoppingCartDtoResponse = exchange.getBody();
        logger.info("ShoppingCart saved with successful: {}", shoppingCartDtoResponse);
        final ShoppingCartResponse shoppingCartResponse = this.shoppingCartMapper.transform(shoppingCartDtoResponse);
        return ResponseEntity.ok().body(shoppingCartResponse);
    }

    public ResponseEntity<CartItemListResponse> getCartItems(UUID shoppingCartId) {
        logger.info("Searching cart items to shopping cart '{}'", shoppingCartId);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Void> requestEntity = new HttpEntity<>(AuthHelper.getHeaderAuth());
        final ResponseEntity<CartItemDto[]> responseEntity = restTemplate.exchange(System.getenv("BASE_URL") + "/api/v1/cart-items?shoppingCartId=" + shoppingCartId,
                HttpMethod.GET,
                requestEntity,
                CartItemDto[].class);
        CartItemDto[] cartItemDtos = responseEntity.getBody();
        assert cartItemDtos != null;
        List<CartItemDto> cartItemDtoList = Arrays.stream(cartItemDtos).sorted(Comparator.comparing(CartItemDto::getCreatedAt)).collect(Collectors.toList());
        final CartItemListResponse cartItemListResponse = this.cartItemMapper.toCartItemListResponse(cartItemDtoList);
        return ResponseEntity.ok().body(cartItemListResponse);
    }

    public ResponseEntity addCartItem(UUID shoppingCartId, CartItemRequest cartItemRequest) throws ParseException {
        RestTemplate restTemplate = new RestTemplate();
        final CartItemDto cartItemDto = this.cartItemMapper.transform(cartItemRequest);
        cartItemDto.setShoppingCart(ShoppingCartDto.builder().id(shoppingCartId).build());
        HttpEntity requestEntity = new HttpEntity(cartItemDto, AuthHelper.getHeaderAuth());
        final ResponseEntity<CartItemDto> responseEntity = restTemplate.exchange(System.getenv("BASE_URL") + "/api/v1/shopping-carts/cart-item",
                HttpMethod.POST,
                requestEntity,
                CartItemDto.class);
        final CartItemDto cartItemDtoSaved = responseEntity.getBody();
        final CartItemResponse shoppingCartResponse = this.cartItemMapper.transform(cartItemDtoSaved);
        return ResponseEntity.ok().body(shoppingCartResponse);
    }

    public ResponseEntity updateCartItem(UUID shoppingCartId, CartItemRequest cartItemRequest) throws ParseException {
        RestTemplate restTemplate = new RestTemplate();
        final CartItemDto cartItemDto = this.cartItemMapper.transform(cartItemRequest);
        cartItemDto.setShoppingCart(ShoppingCartDto.builder().id(shoppingCartId).build());
        HttpEntity<CartItemDto> entity = new HttpEntity<CartItemDto>(cartItemDto, AuthHelper.getHeaderAuth());
        final ResponseEntity<CartItemDto> response = restTemplate.exchange(System.getenv("BASE_URL") + "/api/v1/shopping-carts/cart-item",
                HttpMethod.PUT,
                entity,
                CartItemDto.class);
        final CartItemResponse shoppingCartResponse = this.cartItemMapper.transform(response.getBody());
        return ResponseEntity.ok().body(shoppingCartResponse);
    }

    public ResponseEntity update(ShoppingCartDto shoppingCartDto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ShoppingCartDto> entity = new HttpEntity<ShoppingCartDto>(shoppingCartDto, AuthHelper.getHeaderAuth());
        ResponseEntity<ShoppingCartDto> shoppingCartDtoResponse = restTemplate.exchange(System.getenv("BASE_URL") + "/api/v1/shopping-carts"
                , HttpMethod.PUT
                , entity
                , ShoppingCartDto.class);
        final ShoppingCartResponse shoppingCartResponse = this.shoppingCartMapper.transform(shoppingCartDtoResponse.getBody());
        return ResponseEntity.ok().body(shoppingCartResponse);
    }

    public boolean deleteCartItem(UUID id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity entity = new HttpEntity<ShoppingCartDto>(AuthHelper.getHeaderAuth());
        restTemplate.exchange(System.getenv("BASE_URL") + "/api/v1/cart-items/" + id.toString()
                , HttpMethod.DELETE
                , entity
                , String.class);
        return true;
    }
}
