package lsmsdb.unipi.it.virtualtrade.controller;

import lombok.RequiredArgsConstructor;
import lsmsdb.unipi.it.virtualtrade.dto.TradeRequestDTO;
import lsmsdb.unipi.it.virtualtrade.service.TradingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/trading")
@RequiredArgsConstructor // Automatically generates the constructor for tradingService
public class TradingController {

    private final TradingService tradingService;

    /**
     * Executes a BUY operation against the Redis hot cache.
     * Triggers an asynchronous update to the MongoDB cold storage.
     */
    @PostMapping("/buy")
    public ResponseEntity<Void> buy(@RequestBody TradeRequestDTO dto) {
        tradingService.buy(dto);
        return ResponseEntity.ok().build();
    }

    /**
     * Executes a SELL operation against the Redis hot cache.
     * Triggers an asynchronous update to the MongoDB cold storage.
     */
    @PostMapping("/sell")
    public ResponseEntity<Void> sell(@RequestBody TradeRequestDTO dto) {
        tradingService.sell(dto);
        return ResponseEntity.ok().build();
    }
}