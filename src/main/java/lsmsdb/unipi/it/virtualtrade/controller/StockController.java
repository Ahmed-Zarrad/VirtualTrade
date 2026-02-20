package lsmsdb.unipi.it.virtualtrade.controller;

import lsmsdb.unipi.it.virtualtrade.dto.StockDTO;
import lsmsdb.unipi.it.virtualtrade.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StockController {

    private final StockService stockService;


     // http://localhost:8080/api/stocks

    @GetMapping
    public ResponseEntity<List<StockDTO>> getAllStocks() {
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    //GET http://localhost:8080/api/stocks/AAPL

    @GetMapping("/{symbol}")
    public ResponseEntity<StockDTO> getStock(@PathVariable String symbol) {
        return ResponseEntity.ok(stockService.getStock(symbol));
    }


      // POST http://localhost:8080/api/stocks/AAPL/price?newPrice=155.50

    @PostMapping("/{symbol}/price")
    public ResponseEntity<Void> updatePrice(@PathVariable String symbol, @RequestParam Double newPrice) {
        stockService.updateStockPrice(symbol, newPrice);
        return ResponseEntity.ok().build();
    }
}