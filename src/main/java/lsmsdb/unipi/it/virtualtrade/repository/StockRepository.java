package lsmsdb.unipi.it.virtualtrade.repository;

import lsmsdb.unipi.it.virtualtrade.model.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends MongoRepository<Stock, String> {

    // 1. Standard findById() works perfectly because @Id is the symbol.
    // usage: repository.findById("AAPL");

    // 2. Find Top Gainers (for the Dashboard)
    // Sorts by changePercent descending and takes top 5
    List<Stock> findTop5ByOrderByChangePercentDesc();

    // 3. Find Top Losers
    // Sorts by changePercent ascending and takes top 5
    List<Stock> findTop5ByOrderByChangePercentAsc();

    // 4. Optimization: Find ONLY price and change (exclude the heavy history list)
    // Useful for  "All Stocks" list view where you don't need the chart data yet.
    @Query(value = "{}", fields = "{ 'symbol': 1, 'currentPrice': 1, 'changePercent': 1, 'dailyHistory': 0 }")
    List<Stock> findAllExcludeHistory();
}