package lsmsdb.unipi.it.virtualtrade.repository;

import lsmsdb.unipi.it.virtualtrade.model.MarketDataBucket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.time.LocalDate;
import java.util.Optional;

public interface MarketDataBucketRepository extends MongoRepository<MarketDataBucket, String> {
    @Query("{ 'symbol': ?0, 'date': ?1, }")
    Optional<MarketDataBucket> findBucket(String symbol, LocalDate date);
}