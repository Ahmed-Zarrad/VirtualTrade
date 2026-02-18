package lsmsdb.unipi.it.virtualtrade.repository;

import lsmsdb.unipi.it.virtualtrade.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends MongoRepository<Stock, String> {

    List<Stock> findTop5ByOrderByChangePercentDesc();

    List<Stock> findTop5ByOrderByChangePercentAsc();

    @Query(value = "{}", fields = "{ 'symbol': 1, 'currentPrice': 1, 'changePercent': 1, 'dailyHistory': 0 }")
    List<Stock> findAllExcludeHistory();
}