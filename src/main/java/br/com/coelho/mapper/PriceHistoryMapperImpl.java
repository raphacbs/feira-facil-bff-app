package br.com.coelho.mapper;

import br.com.coelho.dto.PriceHistoryDto;
import br.com.coelho.dto.response.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


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

    @Override
    public PriceHistoryDto transform(PriceHistoryResponse priceHistoryResponse) throws ParseException {
        if (priceHistoryResponse != null){
            return PriceHistoryDto.builder()
                    .date(parseToLocalDateTime(priceHistoryResponse.getDate()))
                    .id(UUID.fromString(priceHistoryResponse.getId()))
                    .price(parseToDouble(priceHistoryResponse.getPrice()))
                    .supermarketId(UUID.fromString(priceHistoryResponse.getSupermarketId()))
                    .productId(UUID.fromString(priceHistoryResponse.getProductId()))
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

    private LocalDateTime parseToLocalDateTime(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date, formatter).atTime(LocalTime.now());
    }
    private double parseToDouble(String value) throws ParseException {
        value = value.replace("R$ ","");
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        Number number = format.parse(value);
        return number.doubleValue();
    }


}
