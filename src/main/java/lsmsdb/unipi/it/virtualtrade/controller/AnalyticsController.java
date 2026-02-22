package lsmsdb.unipi.it.virtualtrade.controller;

import lombok.RequiredArgsConstructor;
import lsmsdb.unipi.it.virtualtrade.dto.AnalyticsResponseDTO;
import lsmsdb.unipi.it.virtualtrade.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/top-traded")
    public ResponseEntity<List<AnalyticsResponseDTO>> getTopTradedStocks(
            @RequestParam(value = "days", defaultValue = "30") int days,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.getTopTradedStocks(days, limit));
    }

    @GetMapping("/high-volatility")
    public ResponseEntity<List<AnalyticsResponseDTO>> getMostVolatileStocks(
            @RequestParam(value = "days", defaultValue = "30") int days,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.getMostVolatileStocks(days, limit));
    }

    @GetMapping("/top-movers")
    public ResponseEntity<List<AnalyticsResponseDTO>> getTopMovers() {
        List<AnalyticsResponseDTO> topMovers = analyticsService.getTopMovers();
        return ResponseEntity.ok(topMovers);
    }
}