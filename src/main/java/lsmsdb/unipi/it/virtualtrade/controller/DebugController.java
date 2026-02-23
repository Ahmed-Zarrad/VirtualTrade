package lsmsdb.unipi.it.virtualtrade.controller;

import lombok.RequiredArgsConstructor;
import lsmsdb.unipi.it.virtualtrade.model.*;
import lsmsdb.unipi.it.virtualtrade.repository.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
public class DebugController {

    private final UserRepository userRepository;
    private final MongoPortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;
    private final StockMetadataRepository stockMetadataRepository;
    private final MarketDataBucketRepository marketDataBucketRepository;
    private final NewsArticleRepository newsArticleRepository;
    private final TransactionRepository transactionRepository;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/portfolios")
    public List<MongoPortfolio> getAllPortfolios() {
        return portfolioRepository.findAll();
    }

    @GetMapping("/stocks")
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @GetMapping("/stock-metadata")
    public List<StockMetadata> getAllStockMetadata() {
        return stockMetadataRepository.findAll();
    }

    @GetMapping("/market-buckets")
    public List<MarketDataBucket> getAllMarketBuckets() {
        return marketDataBucketRepository.findAll();
    }

    @GetMapping("/news")
    public List<NewsArticle> getAllNews() {
        return newsArticleRepository.findAll();
    }

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}