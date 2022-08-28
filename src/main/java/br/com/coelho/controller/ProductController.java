package br.com.coelho.controller;

import br.com.coelho.dto.request.ProductRequest;
import br.com.coelho.dto.response.ProductListResponse;
import br.com.coelho.dto.response.ProductResponse;
import br.com.coelho.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.GeneralSecurityException;
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
    public ResponseEntity<ProductListResponse> get(@RequestParam(value = "ean", required = false) String ean, @RequestParam(value = "description", required = false) String description) throws Exception {
        final Optional<ProductListResponse> product = this.productService.get(ProductRequest.builder().ean(ean).description(description).build());
        return product.map(productResponse -> ResponseEntity.status(HttpStatus.OK).body(productResponse))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PostMapping()
    public ResponseEntity<ProductResponse> create( @RequestParam("photo") MultipartFile photo, @RequestParam String data) throws IOException, GeneralSecurityException {
        Optional<ProductResponse> productDtoSaved= this.productService.create(data, photo);
        return productDtoSaved.map(productResponse -> ResponseEntity.status(HttpStatus.CREATED).body(productResponse))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PutMapping()
    public ResponseEntity<ProductResponse> insertToShoppingCart( @RequestParam("photo") MultipartFile photo, @RequestParam String product) throws IOException, GeneralSecurityException {
        Optional<ProductResponse> productDtoSaved= this.productService.update(photo, product);
        return productDtoSaved.map(productResponse -> ResponseEntity.status(HttpStatus.CREATED).body(productResponse))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }


}
