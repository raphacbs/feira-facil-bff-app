package br.com.coelho.service;

import br.com.coelho.config.FileStorageProperties;
import br.com.coelho.dto.ProductCosmoDto;
import br.com.coelho.dto.ProductDto;
import br.com.coelho.helper.AuthHelper;
import br.com.coelho.helper.GoogleHelper;
import br.com.coelho.mapper.ProductMapper;
import br.com.coelho.request.ProductRequest;
import br.com.coelho.response.ProductResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
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
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public ProductService(FileStorageProperties fileStorageProperties) throws IOException {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    public Optional<ProductResponse> getByEan(String ean) {
        logger.info("Searching product by EAN: {}", ean);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Void> requestEntity = new HttpEntity<>(AuthHelper.getHeaderAuth());
        final ResponseEntity<ProductDto> responseEntity = restTemplate.exchange(System.getenv("BASE_URL") + "/api/v1/products?ean=" + ean,
                HttpMethod.GET,
                requestEntity,
                ProductDto.class);
        if(responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.NO_CONTENT){
            logger.error("Error getByEna in backend: {} ",  (responseEntity.hasBody() ? responseEntity.getBody() : responseEntity.getStatusCodeValue()));
            return Optional.empty();
        }
        ProductDto productDto = responseEntity.getBody();
        if (productDto == null) {
            logger.info("Product not found in database. Searching in Cosmo API.");
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Cosmos-Token", System.getenv("X_COSMOS_TOKEN"));
            HttpEntity<Void> requestCosmo = new HttpEntity<>(headers);
            try {
                final ResponseEntity<ProductCosmoDto> productCosmoDtoResponseEntity = restTemplate.exchange(
                        "https://api.cosmos.bluesoft.com.br/gtins/" + ean, HttpMethod.GET, requestCosmo, ProductCosmoDto.class);
                if(productCosmoDtoResponseEntity.getStatusCode() == HttpStatus.NOT_FOUND){
                    logger.info("Product with ean '{}' not found in Cosmo API", ean);
                }
                logger.info("Product with ean '{}' found in Cosmo API", ean);
                final ProductRequest productRequest = productMapper.transfome(productCosmoDtoResponseEntity.getBody());
                return save(productRequest);
            } catch (HttpStatusCodeException exception) {
                logger.error("Error when searched in Cosomo API.");
                logger.error("Error: Code:{}, Message:{}", exception.getStatusCode().value(), exception.getMessage());
            }
        }
        final ProductResponse productResponse = productMapper.transfome(productDto);
        return Optional.ofNullable(productResponse);
    }

    public Optional<ProductResponse> save(ProductRequest productRequest) {
        final ProductDto productDto = this.productMapper.transfome(productRequest);
        return save(productDto);
    }

    public Optional<ProductResponse> save(ProductDto productDto) {
        HttpEntity<ProductDto> requestEntity = new HttpEntity<ProductDto>(productDto,AuthHelper.getHeaderAuth());
        RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<ProductDto> responseEntity = restTemplate.exchange(System.getenv("BASE_URL") + "/api/v1/products",
                HttpMethod.POST,
                requestEntity,
                ProductDto.class);
        if(responseEntity.getStatusCode() != HttpStatus.CREATED){
            logger.error("Not saved product: {}", productDto);
            return Optional.empty();
        }
        logger.info("Product saved with success!");
        final ProductDto productDtoSaved = responseEntity.getBody();
        logger.debug("Product: {}", productDtoSaved);
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
