package br.com.coelho.controller;
import br.com.coelho.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/v1/cart-items")
public class CartItemController {
    final private ShoppingCartService shoppingCartService;
@Autowired
    public CartItemController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @DeleteMapping("/{id:^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$}")
    public ResponseEntity deleteCartItem(@PathVariable("id") UUID id) throws ParseException {
        boolean isDeleted =  this.shoppingCartService.deleteCartItem(id);
        return ResponseEntity.ok(isDeleted);
    }
}
