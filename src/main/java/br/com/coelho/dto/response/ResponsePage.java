package br.com.coelho.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public abstract class ResponsePage<T> implements Serializable {
    private List<T> content;
    private int pageNo = 0;
    private int pageSize = 10;
    private long totalElements = 0;
    private int totalPages = 0;
    private boolean last = false;

}
