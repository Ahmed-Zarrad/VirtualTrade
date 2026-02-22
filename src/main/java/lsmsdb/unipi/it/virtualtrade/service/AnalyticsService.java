package lsmsdb.unipi.it.virtualtrade.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lsmsdb.unipi.it.virtualtrade.dto.AnalyticsResponseDTO;
import lsmsdb.unipi.it.virtualtrade.repository.MarketDataBucketRepository;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final MarketDataBucketRepository marketDataRepository;

    @Cacheable(value = "analytics:volume", key = "#days + '-' + #limit")
    public List<AnalyticsResponseDTO> getTopTradedStocks(int days, int limit) {
        Instant end = Instant.now();
        Instant start = end.minus(days, ChronoUnit.DAYS);

        return marketDataRepository.findTopTradedStocks(start, end, limit);
    }

    @Cacheable(value = "analytics:volatility", key = "#days + '-' + #limit")
    public List<AnalyticsResponseDTO> getMostVolatileStocks(int days, int limit) {
        Instant end = Instant.now();
        Instant start = end.minus(days, ChronoUnit.DAYS);

        return marketDataRepository.findMostVolatileStocks(start, end, limit);
    }
    @Cacheable(value = "analytics:movers", key = "'top'")
    public List<AnalyticsResponseDTO> getTopMovers() {
        Instant end = Instant.now();
        Instant start = end.minus(1, ChronoUnit.DAYS);
        int limit = 5;

        return marketDataRepository.findTopMovers(start, end, limit);
    }
}