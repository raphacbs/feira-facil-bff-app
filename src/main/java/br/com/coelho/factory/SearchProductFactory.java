package br.com.coelho.factory;

import br.com.coelho.enums.EnumSearchProduct;
import br.com.coelho.mapper.ProductMapper;
import br.com.coelho.service.SearchProduct;
import br.com.coelho.service.SearchProductByDescription;
import br.com.coelho.service.SearchProductByEan;
import br.com.coelho.service.SearchProductCosmo;
import org.springframework.stereotype.Service;

@Service
public class SearchProductFactory {
    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    public  SearchProduct create(EnumSearchProduct enumSearchProduct) throws Exception {
        switch (enumSearchProduct){
            case ByEan -> {
                return SearchProductByEan.builder().build();
            }
            case InCosmo -> {
                return SearchProductCosmo.builder().build();
            }
            case ByDescription -> {
                return SearchProductByDescription.builder().build();
            }
            default -> throw new Exception("Not implemented");
        }
    }
}
