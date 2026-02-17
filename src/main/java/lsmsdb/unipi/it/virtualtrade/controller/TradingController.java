package lsmsdb.unipi.it.virtualtrade.controller;

import lsmsdb.unipi.it.virtualtrade.dto.TradeRequestDTO;
import lsmsdb.unipi.it.virtualtrade.service.TradingService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trading")
public class TradingController {

    private final TradingService tradingService;

    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    /**
     * Executes a BUY operation.
     */
    @PostMapping("/buy")
    public ResponseEntity<Void> buy(@RequestBody TradeRequestDTO dto) {
        tradingService.buy(dto);
        return ResponseEntity.ok().build();
    }

    /**
     * Executes a SELL operation.
     */
    @PostMapping("/sell")
    public ResponseEntity<Void> sell(@RequestBody TradeRequestDTO dto) {
        tradingService.sell(dto);
        return ResponseEntity.ok().build();
    }
}


