package lsmsdb.unipi.it.virtualtrade.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lsmsdb.unipi.it.virtualtrade.dto.TransactionResponseDTO;
import lsmsdb.unipi.it.virtualtrade.model.MongoPortfolio;
import lsmsdb.unipi.it.virtualtrade.model.Transaction;
import lsmsdb.unipi.it.virtualtrade.repository.MongoPortfolioRepository;
import lsmsdb.unipi.it.virtualtrade.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MongoPortfolioRepository mongoPortfolioRepository;

    /**
     * Handles Mongo update after a trade.
     * Implements the Subset Pattern: Keeps 10 embedded, links the rest.
     */
    public void updateMongoAfterTrade(Transaction transaction) {

        // 1. Save transaction to get its generated ID
        Transaction saved = transactionRepository.save(transaction);

        // 2. Retrieve portfolio
        MongoPortfolio portfolio = mongoPortfolioRepository
                .findById(transaction.getUserId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        // 3. THE SUBSET PATTERN LOGIC
        List<Transaction> recent = portfolio.getRecentTransactions();
        recent.add(0, saved); // Push newest to the front

        // If it exceeds 10, pop the oldest and link its ID
        if (recent.size() > 10) {
            Transaction oldest = recent.remove(recent.size() - 1);
            portfolio.getPastTransactionIds().add(oldest.getId());
        }

        // 4. Update holdings in Mongo (Cold Storage Snapshot)
        List<MongoPortfolio.Holding> holdings = portfolio.getHoldings();
        MongoPortfolio.Holding existing = holdings.stream()
                .filter(h -> h.getSymbol().equals(transaction.getSymbol()))
                .findFirst()
                .orElse(null);

        BigDecimal total = transaction.getExecutionPrice()
                .multiply(BigDecimal.valueOf(transaction.getQuantity()));

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
                BigDecimal newAvg = weighted.divide(BigDecimal.valueOf(newQty), 6, RoundingMode.HALF_UP);

                existing.setQuantity(newQty);
                existing.setAverageBuyPrice(newAvg);
            }
            portfolio.setCashBalance(portfolio.getCashBalance().subtract(total));

        } else { // SELL
            if (existing != null) {
                int newQty = existing.getQuantity() - transaction.getQuantity();
                if (newQty <= 0) {
                    holdings.remove(existing);
                } else {
                    existing.setQuantity(newQty);
                }
            }
            portfolio.setCashBalance(portfolio.getCashBalance().add(total));
        }

        mongoPortfolioRepository.save(portfolio);
    }

    /**
     * Returns the most recent transactions embedded in the portfolio document.
     */
    public List<TransactionResponseDTO> getRecentTransactions(String userId) {
        MongoPortfolio portfolio = mongoPortfolioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        return portfolio.getRecentTransactions().stream().map(this::toDTO).toList();
    }

    /**
     * Returns full older transaction history using the Linked IDs.
     */
    public Page<TransactionResponseDTO> getTransactionHistory(String userId, int page, int size) {
        MongoPortfolio portfolio = mongoPortfolioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        List<String> pastIds = portfolio.getPastTransactionIds();
        if (pastIds == null || pastIds.isEmpty()) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findByIdInOrderByTimestampDesc(pastIds, pageable).map(this::toDTO);
    }

    /**
     * Returns older transaction history filtered by stock symbol using the Linked IDs.
     */
    public Page<TransactionResponseDTO> getTransactionsBySymbol(String userId, String symbol, int page, int size) {
        MongoPortfolio portfolio = mongoPortfolioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        List<String> pastIds = portfolio.getPastTransactionIds();
        if (pastIds == null || pastIds.isEmpty()) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findByIdInAndSymbolOrderByTimestampDesc(pastIds, symbol, pageable).map(this::toDTO);
    }

    private TransactionResponseDTO toDTO(Transaction t) {
        return TransactionResponseDTO.builder()
                .id(t.getId())
                .symbol(t.getSymbol())
                .type(t.getType().name())
                .quantity(t.getQuantity())
                .executionPrice(t.getExecutionPrice())
                .timestamp(t.getTimestamp())
                .build();
    }
}