package lsmsdb.unipi.it.virtualtrade.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lsmsdb.unipi.it.virtualtrade.model.RedisPortfolio;
import lsmsdb.unipi.it.virtualtrade.model.Transaction;
import lsmsdb.unipi.it.virtualtrade.dto.TradeRequestDTO;
import lsmsdb.unipi.it.virtualtrade.repository.RedisPortfolioRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradingService {

    private final RedisPortfolioRepository redisPortfolioRepository;
    private final TransactionService transactionService;
    private final RedisTemplate<String, String> redisTemplate;

    public void buy(TradeRequestDTO dto) {

        RedisPortfolio portfolio = redisPortfolioRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        String priceStr = (String) redisTemplate.opsForHash().get("prices", dto.getSymbol());

        if (priceStr == null) {
            throw new RuntimeException("Price not available");
        }

        BigDecimal currentPrice = new BigDecimal(priceStr);
        BigDecimal quantityBD = BigDecimal.valueOf(dto.getQuantity());
        BigDecimal totalCost = currentPrice.multiply(quantityBD);

        if (portfolio.getCashBalance().compareTo(totalCost) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // Update cash
        portfolio.setCashBalance(portfolio.getCashBalance().subtract(totalCost));

        // Update holdings list
        List<RedisPortfolio.Holding> holdings = portfolio.getHoldings();
        if (holdings == null) {
            holdings = new ArrayList<>();
            portfolio.setHoldings(holdings);
        }

        RedisPortfolio.Holding existing = holdings.stream()
                .filter(h -> h.getSymbol().equals(dto.getSymbol()))
                .findFirst()
                .orElse(null);

        if (existing == null) {
            // New holding
            holdings.add(new RedisPortfolio.Holding(dto.getSymbol(), dto.getQuantity(), currentPrice));
        } else {
            // Update weighted average price
            BigDecimal oldQty = BigDecimal.valueOf(existing.getQuantity());
            BigDecimal newQty = BigDecimal.valueOf(dto.getQuantity());

            BigDecimal totalOldValue = existing.getAverageBuyPrice().multiply(oldQty);
            BigDecimal totalNewValue = currentPrice.multiply(newQty);
            BigDecimal updatedQty = oldQty.add(newQty);

            BigDecimal newAverage = totalOldValue.add(totalNewValue)
                    .divide(updatedQty, 6, RoundingMode.HALF_UP);

            existing.setQuantity(updatedQty.intValue());
            existing.setAverageBuyPrice(newAverage);
        }

        portfolio.setLastUpdated(Instant.now());
        redisPortfolioRepository.save(portfolio);

        // Create Transaction using the new Builder pattern (No portfolioId needed!)
        Transaction transaction = Transaction.builder()
                .userId(dto.getUserId())
                .symbol(dto.getSymbol())
                .type(Transaction.TransactionType.BUY)
                .quantity(dto.getQuantity())
                .executionPrice(currentPrice)
                .timestamp(Instant.now())
                .build();

        // Pass to the "Cold" layer for persistent storage
        transactionService.updateMongoAfterTrade(transaction);
    }

    public void sell(TradeRequestDTO dto) {

        RedisPortfolio portfolio = redisPortfolioRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        String priceStr = (String) redisTemplate.opsForHash().get("prices", dto.getSymbol());

        if (priceStr == null) {
            throw new RuntimeException("Price not available");
        }

        BigDecimal currentPrice = new BigDecimal(priceStr);
        BigDecimal quantityBD = BigDecimal.valueOf(dto.getQuantity());
        BigDecimal totalGain = currentPrice.multiply(quantityBD);

        List<RedisPortfolio.Holding> holdings = portfolio.getHoldings();

        RedisPortfolio.Holding existing = holdings.stream()
                .filter(h -> h.getSymbol().equals(dto.getSymbol()))
                .findFirst()
                .orElse(null);

        if (existing == null || existing.getQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Not enough shares");
        }

        int remaining = existing.getQuantity() - dto.getQuantity();

        if (remaining > 0) {
            existing.setQuantity(remaining);
        } else {
            holdings.remove(existing);
        }

        portfolio.setCashBalance(portfolio.getCashBalance().add(totalGain));
        portfolio.setLastUpdated(Instant.now());

        redisPortfolioRepository.save(portfolio);

        Transaction transaction = Transaction.builder()
                .userId(dto.getUserId())
                .symbol(dto.getSymbol())
                .type(Transaction.TransactionType.SELL)
                .quantity(dto.getQuantity())
                .executionPrice(currentPrice)
                .timestamp(Instant.now())
                .build();

        transactionService.updateMongoAfterTrade(transaction);
    }
}