package lsmsdb.unipi.it.virtualtrade.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lsmsdb.unipi.it.virtualtrade.dto.AnalyticsResponseDTO;
import lsmsdb.unipi.it.virtualtrade.repository.MarketDataBucketRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final MarketDataBucketRepository marketDataRepository;


    public List<AnalyticsResponseDTO> getTopTradedStocks(int days, int limit) {
        Instant end = Instant.now();
        Instant start = end.minus(days, ChronoUnit.DAYS);

        return marketDataRepository.findTopTradedStocks(start, end, limit);
    }


    public List<AnalyticsResponseDTO> getMostVolatileStocks(int days, int limit) {
        Instant end = Instant.now();
        Instant start = end.minus(days, ChronoUnit.DAYS);

        return marketDataRepository.findMostVolatileStocks(start, end, limit);
    }
}