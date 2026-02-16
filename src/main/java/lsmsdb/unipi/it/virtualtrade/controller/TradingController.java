package lsmsdb.unipi.it.virtualtrade.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import lsmsdb.unipi.it.virtualtrade.dto.TradeRequestDTO;
import lsmsdb.unipi.it.virtualtrade.service.TradingService;

@RestController
@RequestMapping("/api/trading")
public class TradingController {

    private final TradingService tradingService;

    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @PostMapping("/buy")
    public ResponseEntity<String> buy(@RequestBody TradeRequestDTO dto) {

        tradingService.buy(dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Buy executed successfully");
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sell(@RequestBody TradeRequestDTO dto) {

        tradingService.sell(dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Sell executed successfully");
    }
}

