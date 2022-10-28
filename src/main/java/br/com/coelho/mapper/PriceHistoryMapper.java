package br.com.coelho.mapper;

import br.com.coelho.dto.PriceHistoryDto;
import br.com.coelho.dto.ProductDto;
import br.com.coelho.dto.response.PriceHistoryResponse;
import br.com.coelho.dto.response.PriceHistoryResponsePage;
import br.com.coelho.dto.response.PriceHistoryResponsePageInfo;
import br.com.coelho.dto.response.ProductListResponse;
import org.mapstruct.factory.Mappers;

import java.util.List;

public interface PriceHistoryMapper {
    PriceHistoryMapper INSTANCE = Mappers.getMapper(PriceHistoryMapper.class);
    PriceHistoryResponsePageInfo transform(PriceHistoryResponsePage priceHistoryResponsePage);
    List<PriceHistoryResponse> transform(List<PriceHistoryDto> priceHistoryDtos);
    PriceHistoryResponse transform(PriceHistoryDto priceHistoryDto);

}
