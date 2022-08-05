package br.com.coelho.controller;

import br.com.coelho.dto.ProductDto;
import br.com.coelho.dto.ShoppingCartDto;
import br.com.coelho.response.ShoppingCardProductsResponse;
import br.com.coelho.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity get() {
        return ResponseEntity.ok().body(this.shoppingCartService.getAll());
    }

    @PostMapping
    public ResponseEntity create(@RequestBody ShoppingCartDto shoppingCartDto) {
        return this.shoppingCartService.create(shoppingCartDto);
    }

    @PostMapping("{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}/products")
    public ResponseEntity addProduct(@PathVariable("id") UUID id, @RequestBody ProductDto productDto) {
//        return this.shoppingCartService.addProduct(id, productDto);
        return null;
    }

    @GetMapping("/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}/products")
    public ResponseEntity<List<ShoppingCardProductsResponse>> getProducts(@PathVariable("id") UUID shoppingCartId){
        return this.shoppingCartService.getProducts(shoppingCartId);
    }
}
