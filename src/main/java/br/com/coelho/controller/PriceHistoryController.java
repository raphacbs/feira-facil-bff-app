package br.com.coelho.controller;

import br.com.coelho.dto.response.PriceHistoryResponse;
import br.com.coelho.dto.response.PriceHistoryResponsePageInfo;
import br.com.coelho.service.PriceHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/v1/price-histories")
public class PriceHistoryController {
    private final PriceHistoryService priceHistoryService;

    public PriceHistoryController(PriceHistoryService priceHistoryService) {
        this.priceHistoryService = priceHistoryService;
    }

    @GetMapping()
    public ResponseEntity<PriceHistoryResponsePageInfo> getPriceHistory(@RequestParam(value = "supermarketId", required = false) String supermarketId,
                                                                        @RequestParam(value = "productId") String productId,
                                                                        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                        @RequestParam(value = "sortBy", defaultValue = "date", required = false) String sortBy,
                                                                        @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir){
        return ResponseEntity.ok(this.priceHistoryService.getPriceHistory(pageNo, pageSize, sortBy, sortDir, productId, supermarketId));
    }
}
