package br.com.coelho.service;

import br.com.coelho.config.FileStorageProperties;
import br.com.coelho.dto.ProductDto;
import br.com.coelho.dto.response.ProductListResponse;
import br.com.coelho.enums.EnumSearchProduct;
import br.com.coelho.factory.SearchProductFactory;
import br.com.coelho.helper.AuthHelper;
import br.com.coelho.helper.GoogleHelper;
import br.com.coelho.mapper.ProductMapper;
import br.com.coelho.dto.request.ProductRequest;
import br.com.coelho.dto.response.ProductResponse;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductMapper productMapper = ProductMapper.INSTANCE;
    private final Path fileStorageLocation;
    private final SearchProductFactory searchProductFactory;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private static String fileId = "1w361FjVApKKJn6g8H5NVZ3IVbL-fSpo4";

    @Autowired
    public ProductService(FileStorageProperties fileStorageProperties, SearchProductFactory searchProductFactory) throws IOException {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.searchProductFactory = searchProductFactory;
        Files.createDirectories(this.fileStorageLocation);
    }

    public Optional<ProductListResponse> get(ProductRequest productRequest) throws Exception {
        if (productRequest.getEan() != null) {
            return Optional.ofNullable(getByEan(productRequest));
        }if(productRequest.getDescription() != null){
            return Optional.ofNullable(getByDescription(productRequest));
        }
        return Optional.empty();
    }

    private ProductListResponse getByDescription(ProductRequest productRequest) throws Exception {
        SearchProduct searchProduct = searchProductFactory.create(EnumSearchProduct.ByDescription);
        return this.productMapper.transforme(searchProduct.get(productRequest));
    }

    public ProductListResponse getByEan(ProductRequest productRequest) throws Exception {
        SearchProduct searchProduct = searchProductFactory.create(EnumSearchProduct.ByEan);
        List<ProductDto> productDtoList = searchProduct.get(productRequest);
        if (productDtoList.size() > 0) {
           return this.productMapper.transforme(productDtoList);
        } else {
            final SearchProduct searchProductCosmo = searchProductFactory.create(EnumSearchProduct.InCosmo);
            final List<ProductDto> productCosmo = searchProductCosmo.get(productRequest);
            if (productCosmo.size() > 0) {
                final Optional<ProductResponse> productResponse = save(productCosmo.get(0));
                return this.productMapper.transfome(productResponse);
            }
            return null;
        }
    }

    public Optional<ProductResponse> save(ProductRequest productRequest) {
        final ProductDto productDto = this.productMapper.transfome(productRequest);
        return save(productDto);
    }

    public Optional<ProductResponse> save(ProductDto productDto) {
        HttpEntity<ProductDto> requestEntity = new HttpEntity<ProductDto>(productDto, AuthHelper.getHeaderAuth());
        RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<ProductDto> responseEntity = restTemplate.exchange(System.getenv("BASE_URL") + "/api/v1/products",
                HttpMethod.POST,
                requestEntity,
                ProductDto.class);
        if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
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
        if(file != null){
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            fileId = GoogleHelper.uploadBasic(targetLocation.toString());
            Files.delete(targetLocation.toAbsolutePath());
        }
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
