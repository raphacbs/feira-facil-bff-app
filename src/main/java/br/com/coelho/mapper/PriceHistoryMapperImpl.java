package br.com.coelho.mapper;

import br.com.coelho.dto.PriceHistoryDto;
import br.com.coelho.dto.response.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PriceHistoryMapperImpl implements PriceHistoryMapper{
    @Override
    public PriceHistoryResponsePageInfo transform(PriceHistoryResponsePage priceHistoryResponsePage) {
        return PriceHistoryResponsePageInfo.builder()
                .content(transform(priceHistoryResponsePage.getContent()))
                .last(priceHistoryResponsePage.isLast())
                .pageNo(priceHistoryResponsePage.getPageNo())
                .pageSize(priceHistoryResponsePage.getPageSize())
                .totalElements(priceHistoryResponsePage.getTotalElements())
                .totalPages(priceHistoryResponsePage.getTotalPages())
                .build();
    }

    @Override
    public List<PriceHistoryResponse> transform(List<PriceHistoryDto> priceHistoryDtos) {
        List<PriceHistoryResponse> productResponseList = new ArrayList<PriceHistoryResponse>();
        priceHistoryDtos.forEach(productDto -> {
            productResponseList.add(transform(productDto));
        });

        return productResponseList;
    }

    @Override
    public PriceHistoryResponse transform(PriceHistoryDto priceHistoryDto) {
        if(priceHistoryDto != null){
            return PriceHistoryResponse.builder()
                    .id(priceHistoryDto.getId().toString())
                    .price(parseCurrency(priceHistoryDto.getPrice()))
                    .productId(priceHistoryDto.getProductId().toString())
                    .date(formatDate(priceHistoryDto.getDate().format(DateTimeFormatter.ISO_DATE_TIME)))
                    .supermarketId(priceHistoryDto.getSupermarketId().toString())
                    .build();
        }
        return null;
    }

    private String formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(date.replaceAll("\\.\\d+", ""), formatter).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String parseCurrency(final double amount) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.FRANCE);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("#,##0.00");
        return "R$ " + df.format(amount);
    }


}
