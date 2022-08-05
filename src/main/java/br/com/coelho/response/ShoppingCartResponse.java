package br.com.coelho.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartResponse  implements Serializable {
        private String id;
        private String description;
        private String supermarket;
        private String createAt;
        private String updateAt;
        private String amount;
        private int amountProducts;
}
