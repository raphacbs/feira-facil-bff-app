package br.com.coelho.dto.response;

import br.com.coelho.dto.ShoppingCartDto;
import lombok.Builder;

@Builder
public class ShoppingListResponsePage extends ResponsePage<ShoppingCartDto> {
}