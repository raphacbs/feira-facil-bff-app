package br.com.coelho.service;

import br.com.coelho.dto.ShoppingCartDto;
import br.com.coelho.mapper.ShoppingCartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ShoppingCartService {

    private final ShoppingCartMapper shoppingCartProductsMapper = ShoppingCartMapper.INSTANCE;


    public List<ShoppingCartDto> getAll() {
        RestTemplate restTemplate = new RestTemplate();
        ShoppingCartDto[] shoppingCartDtos = restTemplate
                .getForObject("https://feira-facil-dev.herokuapp.com/api/v1/shopping-carts", ShoppingCartDto[].class);
        assert shoppingCartDtos != null;
        List<ShoppingCartDto> shoppingCartDtoList = Arrays.asList(shoppingCartDtos);
        return this.shoppingCartProductsMapper.transform(shoppingCartDtoList);
    }

}
