package lsmsdb.unipi.it.virtualtrade.service;

import lsmsdb.unipi.it.virtualtrade.dto.StockDTO;
import lsmsdb.unipi.it.virtualtrade.model.Stock;
import lsmsdb.unipi.it.virtualtrade.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockPriceCacheService priceCacheService;


    public StockDTO getStock(String symbol) {

        Stock stock = stockRepository.findById(symbol)
                .orElseThrow(() -> new RuntimeException("Stock not found: " + symbol));


        Double cachedPrice = priceCacheService.getCachedPrice(symbol);


        if (cachedPrice != null) {
            stock.setCurrentPrice(cachedPrice);
        }

        return mapToDTO(stock);
    }


    public List<StockDTO> getAllStocks() {

        return stockRepository.findAllExcludeHistory().stream()
                .map(stock -> {

                    Double cachedPrice = priceCacheService.getCachedPrice(stock.getSymbol());
                    if (cachedPrice != null) {
                        stock.setCurrentPrice(cachedPrice);
                    }
                    return mapToDTO(stock);
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void updateStockPrice(String symbol, Double newPrice) {
        Stock stock = stockRepository.findById(symbol)
                .orElseThrow(() -> new RuntimeException("Stock not found: " + symbol));


        double oldPrice = stock.getCurrentPrice();
        double change = 0.0;
        if (oldPrice != 0) {
            change = ((newPrice - oldPrice) / oldPrice) * 100;
        }

        stock.setCurrentPrice(newPrice);
        stock.setChangePercent(change);
        stock.setLastUpdated(Instant.now());


        stockRepository.save(stock);


        priceCacheService.cachePrice(symbol, newPrice);
    }


    private StockDTO mapToDTO(Stock stock) {
        List<StockDTO.DailyCandleDTO> historyDTOs = null;
        if (stock.getDailyHistory() != null) {
            historyDTOs = stock.getDailyHistory().stream()
                    .map(this::mapCandleToDTO)
                    .collect(Collectors.toList());
        }

        return StockDTO.builder()
                .symbol(stock.getSymbol())
                .currentPrice(stock.getCurrentPrice())
                .changePercent(stock.getChangePercent())
                .dailyHistory(historyDTOs)
                .lastUpdated(stock.getLastUpdated())
                .build();
    }

    private StockDTO.DailyCandleDTO mapCandleToDTO(Stock.DailyCandle candle) {
        return StockDTO.DailyCandleDTO.builder()
                .date(candle.getDate())
                .open(candle.getOpen())
                .high(candle.getHigh())
                .low(candle.getLow())
                .close(candle.getClose())
                .volume(candle.getVolume())
                .build();
    }
}