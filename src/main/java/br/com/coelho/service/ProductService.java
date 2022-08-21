package br.com.coelho.service;

import br.com.coelho.config.FileStorageProperties;
import br.com.coelho.dto.ProductCosmoDto;
import br.com.coelho.dto.ProductDto;
import br.com.coelho.helper.GoogleHelper;
import br.com.coelho.mapper.ProductMapper;
import br.com.coelho.request.ProductRequest;
import br.com.coelho.response.ProductResponse;
import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductMapper productMapper = ProductMapper.INSTANCE;
    private final Path fileStorageLocation;

    public ProductService(FileStorageProperties fileStorageProperties) throws IOException {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    public Optional<ProductResponse> getByEan(String ean) {
        RestTemplate restTemplate = new RestTemplate();
        final ProductDto productDto = restTemplate
                .getForObject(System.getenv("BASE_URL") + "/api/v1/products?ean=" + ean, ProductDto.class);
        if (productDto == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Cosmos-Token", System.getenv("X_COSMOS_TOKEN"));
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//            restTemplate.getForObject("https://api.cosmos.bluesoft.com.br/gtins/"+ean, ProductCosmoDto.class);
            final ResponseEntity<ProductCosmoDto> productCosmoDtoResponseEntity = restTemplate.exchange(
                    "https://api.cosmos.bluesoft.com.br/gtins/" + ean, HttpMethod.GET, requestEntity, ProductCosmoDto.class);
            final ProductRequest productRequest = productMapper.transfome(productCosmoDtoResponseEntity.getBody());
            final Optional<ProductResponse> productResponse = save(productRequest);
            return productResponse;
        }
        final ProductResponse productResponse = productMapper.transfome(productDto);
        return Optional.ofNullable(productResponse);
    }

    public Optional<ProductResponse> save(ProductRequest productRequest) {
        final ProductDto productDto = this.productMapper.transfome(productRequest);
        return save(productDto);
    }

    public Optional<ProductResponse> save(ProductDto productDto) {
        RestTemplate restTemplate = new RestTemplate();
        final ProductDto productDtoSaved = restTemplate.postForObject(System.getenv("BASE_URL") + "/api/v1/products"
                , productDto
                , ProductDto.class);
        final ProductResponse productResponseSaved = this.productMapper.transfome(productDtoSaved);
        return Optional.ofNullable(productResponseSaved);
    }

    public Optional<ProductResponse> create(String data, MultipartFile file) throws IOException, GeneralSecurityException {
        Gson gson = new Gson();
        final ProductRequest productRequest = gson.fromJson(data, ProductRequest.class);
        Path targetLocation = this.fileStorageLocation.resolve(productRequest.getEan().toString() + ".jpeg");
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        String fileId = GoogleHelper.uploadBasic(targetLocation.toString());
        Files.delete(targetLocation.toAbsolutePath());
        productRequest.setImage("https://drive.google.com/uc?id=" + fileId);
        return save(productRequest);
    }

    public Optional<ProductResponse> update(MultipartFile file, String product) throws IOException, GeneralSecurityException {
        Gson gson = new Gson();
        final ProductRequest productRequest = gson.fromJson(product, ProductRequest.class);

        Path targetLocation = this.fileStorageLocation.resolve(productRequest.getEan().toString() + ".jpeg");
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        String fileId = GoogleHelper.updateFile(productRequest.getEan(), targetLocation.toString());
        Files.delete(targetLocation.toAbsolutePath());
        productRequest.setImage("https://drive.google.com/uc?id=" + fileId);
        return save(productRequest);
    }
}
