package lsmsdb.unipi.it.virtualtrade.repository;

import lsmsdb.unipi.it.virtualtrade.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByPortfolioId(String portfolioId);
    Page<Transaction> findByPortfolioIdAndSymbolOrderByTimestampDesc(
            String portfolioId,
            String symbol,
            Pageable pageable
    );
    Page<Transaction> findByPortfolioIdOrderByTimestampDesc(
            String portfolioId,
            Pageable pageable
    );
}