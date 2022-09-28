package br.com.coelho.mapper;

import br.com.coelho.dto.ShoppingCartDto;
import br.com.coelho.dto.response.ShoppingListResponse;
import br.com.coelho.dto.response.ShoppingListResponsePage;
import br.com.coelho.dto.response.ShoppingListResponsePageInfo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShoppingListMapperImpl implements ShoppingListMapper {
    @Override
    public ShoppingListResponse transform(ShoppingCartDto shoppingCartDto) {
        return ShoppingListResponse.builder()
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
    public List<ShoppingListResponse> transform(List<ShoppingCartDto> shoppingCartDtos) {
        List<ShoppingListResponse> shoppingCartDtoList = new ArrayList<ShoppingListResponse>();
        shoppingCartDtos.forEach(shoppingCartDto -> {
            shoppingCartDtoList.add(transform(shoppingCartDto));
        });
        return shoppingCartDtoList;
    }

    @Override
    public ShoppingListResponsePageInfo transform(ShoppingListResponsePage shoppingListResponsePage) {
        List<ShoppingListResponse> list = transform(shoppingListResponsePage.getContent());
        return ShoppingListResponsePageInfo.builder()
                .content(list)
                .last(shoppingListResponsePage.isLast())
                .pageNo(shoppingListResponsePage.getPageNo())
                .pageSize(shoppingListResponsePage.getPageSize())
                .totalElements(shoppingListResponsePage.getTotalElements())
                .totalPages(shoppingListResponsePage.getTotalPages())
                .build();

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
