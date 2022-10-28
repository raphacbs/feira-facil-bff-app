package br.com.coelho.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
public class SupermarketResponsePageInfo implements Serializable {
    private List<SupermarketResponse> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
