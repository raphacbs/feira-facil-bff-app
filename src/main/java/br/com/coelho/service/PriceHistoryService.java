package br.com.coelho.service;

import br.com.coelho.dto.response.PriceHistoryResponsePage;
import br.com.coelho.dto.response.PriceHistoryResponsePageInfo;
import br.com.coelho.helper.AuthHelper;
import br.com.coelho.mapper.PriceHistoryMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.isNull;

@Service
public class PriceHistoryService {
    private final PriceHistoryMapper priceHistoryMapper = PriceHistoryMapper.INSTANCE;

    public PriceHistoryResponsePageInfo getPriceHistory(int pageNo, int pageSize, String sortBy, String sortDir, String productId, String supermarketId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Void> requestEntity = new HttpEntity<>(AuthHelper.getHeaderAuth());
        String url = isNull(supermarketId) ? String.format("%s%s?pageNo=%s&pageSize=%s&sortBy=%s&sortDir=%s&productId=%s"
                , System.getenv("BASE_URL")
                , "/api/v1/price-histories"
                , pageNo
                , pageSize
                , sortBy
                , sortDir
                , productId) :
                String.format("%s%s?pageNo=%s&pageSize=%s&sortBy=%s&sortDir=%s&productId=%s&supermarketId=%s"
                        , System.getenv("BASE_URL")
                        , "/api/v1/price-histories"
                        , pageNo
                        , pageSize
                        , sortBy
                        , sortDir
                        , productId, supermarketId);
        final ResponseEntity<PriceHistoryResponsePage> response = restTemplate
                .exchange(url,
                        HttpMethod.GET,
                        requestEntity,
                        PriceHistoryResponsePage.class);

        return this.priceHistoryMapper.transform(response.getBody());
    }

}
