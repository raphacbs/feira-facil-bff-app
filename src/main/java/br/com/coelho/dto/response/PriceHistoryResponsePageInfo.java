package br.com.coelho.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
public class PriceHistoryResponsePageInfo implements Serializable {
    private List<PriceHistoryResponse> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
