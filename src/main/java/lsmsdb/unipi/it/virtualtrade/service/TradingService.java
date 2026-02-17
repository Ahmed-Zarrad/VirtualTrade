package lsmsdb.unipi.it.virtualtrade.service;

import org.springframework.stereotype.Service;

import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

import lsmsdb.unipi.it.virtualtrade.model.RedisPortfolio;
import lsmsdb.unipi.it.virtualtrade.model.Transaction;
import lsmsdb.unipi.it.virtualtrade.dto.TradeRequestDTO;
import lsmsdb.unipi.it.virtualtrade.repository.RedisPortfolioRepository;


@Service
public class TradingService {

    private final RedisPortfolioRepository redisPortfolioRepository;
    private final TransactionService transactionService;
    private final RedisTemplate<String, String> redisTemplate;

    public TradingService(RedisPortfolioRepository redisPortfolioRepository,
                          TransactionService transactionService,
                          RedisTemplate<String, String> redisTemplate) {
        this.redisPortfolioRepository = redisPortfolioRepository;
        this.transactionService = transactionService;
        this.redisTemplate = redisTemplate;
    }

    public void buy(TradeRequestDTO dto) {

        RedisPortfolio portfolio = redisPortfolioRepository.findById(dto.getPortfolioId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        String priceStr = redisTemplate.opsForValue().get("price:" + dto.getSymbol());

        if (priceStr == null) {
            throw new RuntimeException("Price not available");
        }

        BigDecimal currentPrice = new BigDecimal(priceStr);
        BigDecimal quantityBD = BigDecimal.valueOf(dto.getQuantity());
        BigDecimal totalCost = currentPrice.multiply(quantityBD);

        if (portfolio.getCashBalance().compareTo(totalCost) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        //  Update cash
        portfolio.setCashBalance(
                portfolio.getCashBalance().subtract(totalCost)
        );

        //  Update holdings list
        List<RedisPortfolio.Holding> holdings = portfolio.getHoldings();

        RedisPortfolio.Holding existing = null;

        for (RedisPortfolio.Holding h : holdings) {
            if (h.getSymbol().equals(dto.getSymbol())) {
                existing = h;
                break;
            }
        }

        if (existing == null) {
            // new holding
            RedisPortfolio.Holding newHolding =
                    new RedisPortfolio.Holding(
                            dto.getSymbol(),
                            dto.getQuantity(),
                            currentPrice
                    );
            holdings.add(newHolding);

        } else {
            // update weighted average price
            BigDecimal oldQty = BigDecimal.valueOf(existing.getQuantity());
            BigDecimal newQty = BigDecimal.valueOf(dto.getQuantity());

            BigDecimal totalOldValue =
                    existing.getAverageBuyPrice().multiply(oldQty);

            BigDecimal totalNewValue =
                    currentPrice.multiply(newQty);

            BigDecimal updatedQty = oldQty.add(newQty);

            BigDecimal newAverage =
                    totalOldValue.add(totalNewValue)
                            .divide(updatedQty, 6, RoundingMode.HALF_UP);

            existing.setQuantity(updatedQty.intValue());
            existing.setAverageBuyPrice(newAverage);
        }

        portfolio.setLastUpdated(Instant.now());

        redisPortfolioRepository.save(portfolio);

        //  create Transactiion
        Transaction transaction = new Transaction();
        transaction.setPortfolioId(dto.getPortfolioId());
        transaction.setSymbol(dto.getSymbol());
        transaction.setType(Transaction.TransactionType.BUY);
        transaction.setQuantity(dto.getQuantity());
        transaction.setExecutionPrice(currentPrice);
        transaction.setTimestamp(Instant.now());

        transactionService.updateMongoAfterTrade(transaction);
    }
    public void sell(TradeRequestDTO dto) {

        RedisPortfolio portfolio = redisPortfolioRepository.findById(dto.getPortfolioId())
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        String priceStr = redisTemplate.opsForValue().get("price:" + dto.getSymbol());

        if (priceStr == null) {
            throw new RuntimeException("Price not available");
        }

        BigDecimal currentPrice = new BigDecimal(priceStr);
        BigDecimal quantityBD = BigDecimal.valueOf(dto.getQuantity());
        BigDecimal totalGain = currentPrice.multiply(quantityBD);

        List<RedisPortfolio.Holding> holdings = portfolio.getHoldings();

        RedisPortfolio.Holding existing = null;

        for (RedisPortfolio.Holding h : holdings) {
            if (h.getSymbol().equals(dto.getSymbol())) {
                existing = h;
                break;
            }
        }

        if (existing == null || existing.getQuantity() < dto.getQuantity()) {
            throw new RuntimeException("Not enough shares");
        }

        int remaining = existing.getQuantity() - dto.getQuantity();

        if (remaining > 0) {
            existing.setQuantity(remaining);
        } else {
            holdings.remove(existing);
        }

        portfolio.setCashBalance(
                portfolio.getCashBalance().add(totalGain)
        );

        portfolio.setLastUpdated(Instant.now());

        redisPortfolioRepository.save(portfolio);

        Transaction transaction = new Transaction();
        transaction.setPortfolioId(dto.getPortfolioId());
        transaction.setSymbol(dto.getSymbol());
        transaction.setType(Transaction.TransactionType.SELL);
        transaction.setQuantity(dto.getQuantity());
        transaction.setExecutionPrice(currentPrice);
        transaction.setTimestamp(Instant.now());

        transactionService.updateMongoAfterTrade(transaction);
    }
}
