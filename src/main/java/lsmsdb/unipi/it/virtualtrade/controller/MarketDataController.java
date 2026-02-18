package lsmsdb.unipi.it.virtualtrade.controller;

import lsmsdb.unipi.it.virtualtrade.dto.MarketDataBucketDTO;
import lsmsdb.unipi.it.virtualtrade.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/market-data")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MarketDataController {

    private final MarketDataService marketDataService;


     //GET /api/market-data/intraday?symbol=AAPL&date=2026-02-17

    @GetMapping("/intraday")
    public ResponseEntity<MarketDataBucketDTO> getIntradayData(
            @RequestParam String symbol,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {

        if (date == null) {
            date = LocalDate.now();
        }

        return ResponseEntity.ok(marketDataService.getIntradayData(symbol, date));
    }
}