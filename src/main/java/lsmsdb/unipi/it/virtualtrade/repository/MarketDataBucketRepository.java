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



    @Aggregation(pipeline = {
            "{ $match: { date: { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$ticks' }",
            "{ $group: { _id: '$symbol', totalVolume: { $sum: '$ticks.v' } } }",
            "{ $project: { symbol: '$_id', value: '$totalVolume', _id: 0 } }",
            "{ $sort: { value: -1 } }",
            "{ $limit: ?2 }"
    })
    List<AnalyticsResponseDTO> findTopTradedStocks(LocalDate start, LocalDate end, int limit);



    @Aggregation(pipeline = {
            "{ $match: { date: { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$ticks' }",
            "{ $group: { " +
                    "_id: '$symbol', " +
                    "avgVolatility: { $avg: { $subtract: ['$ticks.h', '$ticks.l'] } } " +
                    "} }",
            "{ $project: { symbol: '$_id', value: '$avgVolatility', _id: 0 } }",
            "{ $sort: { value: -1 } }",
            "{ $limit: ?2 }"
    })
    List<AnalyticsResponseDTO> findMostVolatileStocks(LocalDate start, LocalDate end, int limit);




    @Aggregation(pipeline = {
            "{ $match: { date: { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$ticks' }",
            "{ $group: { " +
                    "_id: '$symbol', " +
                    "avgGain: { $avg: { $multiply: [ { $divide: [ { $subtract: ['$ticks.c', '$ticks.o'] }, " +
                    "'$ticks.o' ] }, 100 ] } } " +
                    "} }",
            "{ $project: { symbol: '$_id', value: '$avgGain', _id: 0 } }",
            "{ $sort: { value: -1 } }",
            "{ $limit: ?2 }"
    })
    List<AnalyticsResponseDTO> findTopMovers(LocalDate start, LocalDate end, int limit);
}