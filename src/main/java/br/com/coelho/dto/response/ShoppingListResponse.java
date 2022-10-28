package br.com.coelho.dto.response;

import br.com.coelho.dto.SupermarketDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingListResponse implements Serializable {
        private String id;
        private String description;
        private SupermarketDto supermarket;
        private String createAt;
        private String updateAt;
        private String amount;
        private int amountProducts;
        private boolean isArchived;
        private int amountCheckedProducts;
}
