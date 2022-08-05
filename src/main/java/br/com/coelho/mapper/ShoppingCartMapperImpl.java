package br.com.coelho.mapper;

import br.com.coelho.dto.ShoppingCartDto;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartMapperImpl implements ShoppingCartMapper {
    @Override
    public ShoppingCartDto transform(ShoppingCartDto shoppingCartDto) {
        shoppingCartDto  = ShoppingCartDto.builder()
                .amount(parseCurrency(shoppingCartDto.getAmount()))
                .amountProducts(shoppingCartDto.getAmountProducts())
                .createAt(formatDate(shoppingCartDto.getCreateAt()))
                .description(shoppingCartDto.getDescription())
                .supermarket(shoppingCartDto.getSupermarket())
                .id(shoppingCartDto.getId())
                .updateAt(formatDate(shoppingCartDto.getUpdateAt()))
                .build();
        return shoppingCartDto;
    }

    @Override
    public List<ShoppingCartDto> transform(List<ShoppingCartDto> shoppingCartDtos) {
        List<ShoppingCartDto> shoppingCartDtoList = new ArrayList<ShoppingCartDto>();
        shoppingCartDtos.forEach(shoppingCartDto -> {
            shoppingCartDtoList.add(transform(shoppingCartDto));
        });
        return shoppingCartDtoList;
    }

    private String parseCurrency(final String amount) {
        final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return decimalFormat.format(Double.parseDouble(amount));
    }

    private String formatDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(date.replaceAll("\\.\\d+", ""), formatter).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
