package br.com.coelho.controller;

import br.com.coelho.request.ProductRequest;
import br.com.coelho.response.ProductResponse;
import br.com.coelho.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/v1/products")
public class ProductController {
    final private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @GetMapping()
    public ResponseEntity<ProductResponse> getByEan(@RequestParam("ean") String ean){
        final Optional<ProductResponse> productResponse = this.productService.getByEan(ean);
        return productResponse.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping()
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest){
        Optional<ProductResponse> productDtoSaved = this.productService.create(productRequest);
        return productDtoSaved.map(dto -> ResponseEntity.ok().body(dto)).orElseGet(()
                -> ResponseEntity.noContent().build());
    }
}
