package lsmsdb.unipi.it.virtualtrade.service;

import lsmsdb.unipi.it.virtualtrade.model.Transaction;
import lsmsdb.unipi.it.virtualtrade.model.MongoPortfolio;
import lsmsdb.unipi.it.virtualtrade.repository.TransactionRepository;
import lsmsdb.unipi.it.virtualtrade.repository.MongoPortfolioRepository;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MongoPortfolioRepository mongoPortfolioRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              MongoPortfolioRepository mongoPortfolioRepository) {
        this.transactionRepository = transactionRepository;
        this.mongoPortfolioRepository = mongoPortfolioRepository;
    }

    /**
     * Registers a new transaction.
     *
     * This method:
     * 1. Persists the transaction in the "transactions" collection.
     * 2. Updates the embedded recentTransactions list inside MongoPortfolio.
     */
    public void registerTransaction(Transaction transaction) {

        // Save full transaction history in MongoDB
        Transaction saved = transactionRepository.save(transaction);

        // Retrieve the corresponding portfolio document
        MongoPortfolio portfolio =
                mongoPortfolioRepository.findById(transaction.getPortfolioId())
                        .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        List<Transaction> recent = portfolio.getRecentTransactions();

        // Insert the new transaction at the beginning (most recent first)
        recent.add(0, saved);

        // Keep only the latest 10 transactions
        if (recent.size() > 10) {
            recent.remove(recent.size() - 1);
        }

        // Persist updated portfolio document
        mongoPortfolioRepository.save(portfolio);
    }

    /**
     * Returns the most recent transactions embedded in the portfolio document.
     *
     * This is optimized for dashboard display.
     */
    public List<Transaction> getRecentTransactions(String portfolioId) {

        MongoPortfolio portfolio =
                mongoPortfolioRepository.findById(portfolioId)
                        .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        return portfolio.getRecentTransactions();
    }

    /**
     * Returns full transaction history with pagination.
     */
    public Page<Transaction> getTransactionHistory(String portfolioId,
                                                   int page,
                                                   int size) {

        Pageable pageable = PageRequest.of(page, size);

        return transactionRepository
                .findByPortfolioIdOrderByTimestampDesc(portfolioId, pageable);
    }

    /**
     * Returns paginated transaction history filtered by stock symbol.
     *
     * Useful for filtering transactions (e.g., show only AAPL trades).
     */
    public Page<Transaction> getTransactionsBySymbol(String portfolioId,
                                                     String symbol,
                                                     int page,
                                                     int size) {

        Pageable pageable = PageRequest.of(page, size);

        return transactionRepository
                .findByPortfolioIdAndSymbolOrderByTimestampDesc(
                        portfolioId,
                        symbol,
                        pageable
                );
    }
}

