package br.com.coelho.controller;

import br.com.coelho.dto.ShoppingCartDto;
import br.com.coelho.dto.request.CartItemRequest;
import br.com.coelho.dto.response.CartItemResponsePageInfo;
import br.com.coelho.dto.response.ShoppingListResponsePage;
import br.com.coelho.dto.response.ShoppingListResponsePageInfo;
import br.com.coelho.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/v1/shopping-carts")
public class ShoppingCartController {

    final private ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public ResponseEntity<ShoppingListResponsePageInfo> get(
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        return ResponseEntity.ok().body(this.shoppingCartService.getAll(pageNo, pageSize, sortBy, sortDir));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody ShoppingCartDto shoppingCartDto) {
        return this.shoppingCartService.create(shoppingCartDto);
    }

    @PutMapping
    public ResponseEntity update(@RequestBody ShoppingCartDto shoppingCartDto) {
        return this.shoppingCartService.update(shoppingCartDto);
    }

    @PostMapping("{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}/cart-item")
    public ResponseEntity addCartItem(@PathVariable("id") UUID id, @RequestBody CartItemRequest cartItemRequest) throws ParseException {
        return this.shoppingCartService.addCartItem(id, cartItemRequest);
    }

    @PutMapping("{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}/cart-item")
    public ResponseEntity updateCartItem(@PathVariable("id") UUID id,
                                         @RequestBody CartItemRequest cartItemRequest,
                                         @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                         @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
                                         @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) throws ParseException {
        return this.shoppingCartService.updateCartItem(id, cartItemRequest, pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}/cart-item")
    public ResponseEntity<CartItemResponsePageInfo> getCartItems(
            @PathVariable("id") UUID shoppingCartId,
            @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "createdAt", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir) {
        return this.shoppingCartService.getCartItems(shoppingCartId, pageNo, pageSize, sortBy, sortDir);
    }
}
