package lsmsdb.unipi.it.virtualtrade.repository;

import lsmsdb.unipi.it.virtualtrade.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    // --- THE SUBSET PATTERN QUERIES ---

    /**
     * Fetches the historical transactions using the exact document links (IDs)
     * stored in the MongoPortfolio's pastTransactionIds array.
     */
    Page<Transaction> findByIdInOrderByTimestampDesc(
            List<String> transactionIds,
            Pageable pageable
    );

    /**
     * Fetches the historical transactions for a specific stock symbol,
     * searching ONLY within the linked IDs from the portfolio.
     */
    Page<Transaction> findByIdInAndSymbolOrderByTimestampDesc(
            List<String> transactionIds,
            String symbol,
            Pageable pageable
    );

    // Keep this one just in case you need a raw admin query to find everything a user ever did,
    // but the application logic should rely on the 'findByIdIn' methods above.
    List<Transaction> findByUserId(String userId);
}