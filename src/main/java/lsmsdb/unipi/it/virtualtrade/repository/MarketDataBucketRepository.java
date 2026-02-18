package lsmsdb.unipi.it.virtualtrade.repository;

import lsmsdb.unipi.it.virtualtrade.model.MarketDataBucket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.LocalDate;
import java.util.Optional;

public interface MarketDataBucketRepository extends MongoRepository<MarketDataBucket, String> {
    Optional<MarketDataBucket> findBySymbolAndDate(String symbol, LocalDate date);
}