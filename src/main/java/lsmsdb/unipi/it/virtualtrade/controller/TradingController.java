package lsmdb.unipi.it.virtualtrade.controller;

import lsmsdb.unipi.it.virtualtrade.dto.TradeRequestDTO;
import lsmsdb.unipi.it.virtualtrade.service.TradingService;

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
    public void buy(@RequestBody TradeRequestDTO dto) {
        tradingService.buy(dto);
    }

    /**
     * Executes a SELL operation.
     */
    @PostMapping("/sell")
    public void sell(@RequestBody TradeRequestDTO dto) {
        tradingService.sell(dto);
    }
}


