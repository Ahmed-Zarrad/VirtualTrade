package lsmsdb.unipi.it.virtualtrade.service;

import lsmsdb.unipi.it.virtualtrade.model.Transaction;
import lsmsdb.unipi.it.virtualtrade.model.MongoPortfolio;
import lsmsdb.unipi.it.virtualtrade.repository.TransactionRepository;
import lsmsdb.unipi.it.virtualtrade.repository.MongoPortfolioRepository;
import lsmsdb.unipi.it.virtualtrade.dto.TransactionResponseDTO;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.math.RoundingMode;

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
     * 1. Saves the transaction in MongoDB.
     * 2. Updates cash and holdings snapshot inside MongoPortfolio.
     * 3. Updates embedded recentTransactions (max 10).
     */
    public void registerTransaction(Transaction transaction) {

        //  Save full transaction history
        Transaction saved = transactionRepository.save(transaction);

        //  Retrieve portfolio snapshot from Mongo
        MongoPortfolio portfolio = mongoPortfolioRepository
                .findById(transaction.getPortfolioId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        BigDecimal quantityBD = BigDecimal.valueOf(transaction.getQuantity());
        BigDecimal total = transaction.getExecutionPrice().multiply(quantityBD);

        //  Update cash
        if (transaction.getType() == Transaction.TransactionType.BUY) {
            portfolio.setCashBalance(portfolio.getCashBalance().subtract(total));
        } else {
            portfolio.setCashBalance(portfolio.getCashBalance().add(total));
        }

        //  Update holdings snapshot
        List<MongoPortfolio.Holding> holdings = portfolio.getHoldings();
        MongoPortfolio.Holding existing = null;

        for (MongoPortfolio.Holding h : holdings) {
            if (h.getSymbol().equals(transaction.getSymbol())) {
                existing = h;
                break;
            }
        }

        if (transaction.getType() == Transaction.TransactionType.BUY) {

            if (existing == null) {
                holdings.add(new MongoPortfolio.Holding(
                        transaction.getSymbol(),
                        transaction.getQuantity(),
                        transaction.getExecutionPrice()
                ));
            } else {

                int newQty = existing.getQuantity() + transaction.getQuantity();

                BigDecimal weighted = existing.getAverageBuyPrice()
                        .multiply(BigDecimal.valueOf(existing.getQuantity()))
                        .add(total);

                BigDecimal newAvg = weighted.divide(
                        BigDecimal.valueOf(newQty),
                        6,
                        RoundingMode.HALF_UP
                );

                existing.setQuantity(newQty);
                existing.setAverageBuyPrice(newAvg);
            }

        } else { // SELL

            if (existing != null) {
                int newQty = existing.getQuantity() - transaction.getQuantity();

                if (newQty <= 0) {
                    holdings.remove(existing);
                } else {
                    existing.setQuantity(newQty);
                }
            }
        }

        //  Update recent transactions (max 10)
        List<Transaction> recent = portfolio.getRecentTransactions();
        if (recent == null) {
            recent = new ArrayList<>();
            portfolio.setRecentTransactions(recent);
        }

        recent.add(0, saved);
        if (recent.size() > 10) {
            recent.remove(recent.size() - 1);
        }

        //  Persist updated Mongo snapshot
        mongoPortfolioRepository.save(portfolio);
    }


    /**
     * Returns the most recent transactions embedded in the portfolio document.
     * Optimized for dashboard display.
     */
    public List<TransactionResponseDTO> getRecentTransactions(String portfolioId) {

        MongoPortfolio portfolio = mongoPortfolioRepository
                .findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        return portfolio.getRecentTransactions()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Returns full transaction history with pagination.
     */
    public Page<TransactionResponseDTO> getTransactionHistory(String portfolioId,
                                                      int page,
                                                      int size) {

        Pageable pageable = PageRequest.of(page, size);

        return transactionRepository
                .findByPortfolioIdOrderByTimestampDesc(portfolioId, pageable)
                .map(this::toDTO);
    }

    /**
     * Returns paginated transaction history filtered by stock symbol.
     */
    public Page<TransactionResponseDTO> getTransactionsBySymbol(String portfolioId,
                                                        String symbol,
                                                        int page,
                                                        int size) {

        Pageable pageable = PageRequest.of(page, size);

        return transactionRepository
                .findByPortfolioIdAndSymbolOrderByTimestampDesc(
                        portfolioId,
                        symbol,
                        pageable
                )
                .map(this::toDTO);
    }

    /**
     * Converts Transaction model to TransactionDTO.
     */
    private TransactionResponseDTO toDTO(Transaction t) {
        return new TransactionResponseDTO(
                t.getSymbol(),
                t.getType().name(),
                t.getQuantity(),
                t.getExecutionPrice(),
                t.getTimestamp()
        );
    }
}


