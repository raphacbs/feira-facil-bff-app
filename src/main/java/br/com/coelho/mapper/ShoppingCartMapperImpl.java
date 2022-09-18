package br.com.coelho.mapper;

import br.com.coelho.dto.ShoppingCartDto;
import br.com.coelho.dto.response.ShoppingCartResponse;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShoppingCartMapperImpl implements ShoppingCartMapper {
    @Override
    public ShoppingCartResponse transform(ShoppingCartDto shoppingCartDto) {
        return ShoppingCartResponse.builder()
                .amount(parseCurrency(shoppingCartDto.getAmount()))
                .amountProducts(shoppingCartDto.getAmountProducts())
                .createAt(formatDate(shoppingCartDto.getCreateAt()))
                .description(shoppingCartDto.getDescription())
                .supermarket(shoppingCartDto.getSupermarket())
                .id(shoppingCartDto.getId().toString())
                .updateAt(formatDate(shoppingCartDto.getUpdateAt()))
                .isArchived(shoppingCartDto.isArchived())
                .amountCheckedProducts(shoppingCartDto.getAmountCheckedProducts())
                .build();
    }

    @Override
    public List<ShoppingCartResponse> transform(List<ShoppingCartDto> shoppingCartDtos) {
        List<ShoppingCartResponse> shoppingCartDtoList = new ArrayList<ShoppingCartResponse>();
        shoppingCartDtos.forEach(shoppingCartDto -> {
            shoppingCartDtoList.add(transform(shoppingCartDto));
        });
        return shoppingCartDtoList;
    }

    private String parseCurrency(final double amount) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("#,##0.00");
        return "R$ " + df.format(amount);
    }

    private String formatDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(date.replaceAll("\\.\\d+", ""), formatter).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
