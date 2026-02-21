package lsmsdb.unipi.it.virtualtrade.repository;

import lsmsdb.unipi.it.virtualtrade.model.MarketDataBucket;
import lsmsdb.unipi.it.virtualtrade.dto.AnalyticsResponseDTO;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.time.Instant;
import java.util.List;

public interface MarketDataBucketRepository extends MongoRepository<MarketDataBucket, String> {
    Optional<MarketDataBucket> findBySymbolAndDate(String symbol, LocalDate date);

    // Aggregation 1: Top N most traded stocks, filters by date range, sums the daily volumes, and sorts descending

    @Aggregation(pipeline = {
            "{ $match: { timestamp: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: '$symbol', totalVolume: { $sum: '$volume' } } }",
            "{ $project: { symbol: '$_id', value: '$totalVolume', _id: 0 } }",
            "{ $sort: { value: -1 } }",
            "{ $limit: ?2 }"
    })
    List<AnalyticsResponseDTO> findTopTradedStocks(Instant start, Instant end, int limit);

    // Aggregation 2: Highest volatility stocks, filters by date range, calculates average of (high, low), and sorts descending

    @Aggregation(pipeline = {
            "{ $match: { timestamp: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: '$symbol', avgVolatility: { $avg: { $subtract: ['$high', '$low'] } } } }",
            "{ $project: { symbol: '$_id', value: '$avgVolatility', _id: 0 } }",
            "{ $sort: { value: -1 } }",
            "{ $limit: ?2 }"
    })
    List<AnalyticsResponseDTO> findMostVolatileStocks(Instant start, Instant end, int limit);
}