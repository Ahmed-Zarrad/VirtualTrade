package lsmsdb.unipi.it.virtualtrade.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lsmsdb.unipi.it.virtualtrade.dto.PortfolioViewDTO;
import lsmsdb.unipi.it.virtualtrade.model.MongoPortfolio;
import lsmsdb.unipi.it.virtualtrade.model.RedisPortfolio;
import lsmsdb.unipi.it.virtualtrade.repository.MongoPortfolioRepository;
import lsmsdb.unipi.it.virtualtrade.repository.RedisPortfolioRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final RedisPortfolioRepository redisPortfolioRepository;
    private final MongoPortfolioRepository mongoPortfolioRepository;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Returns portfolio view for a given user.
     * If portfolio does not exist, it creates one in BOTH Redis and Mongo.
     */
    public PortfolioViewDTO getPortfolioView(String userId) {

        RedisPortfolio portfolio = redisPortfolioRepository
                .findById(userId)
                .orElseGet(() -> createAndPersistEmptyPortfolio(userId));

        BigDecimal totalValue = portfolio.getCashBalance();
        BigDecimal totalPnL = BigDecimal.ZERO;

        List<PortfolioViewDTO.Position> positions = new ArrayList<>();

        // Get all latest prices from Redis (single access for high performance)
        Map<Object, Object> prices = redisTemplate.opsForHash().entries("prices");

        for (RedisPortfolio.Holding h : portfolio.getHoldings()) {

            String priceStr = (String) prices.get(h.getSymbol());

            if (priceStr == null) {
                log.warn("Price not found in Redis for symbol: {}", h.getSymbol());
                continue;
            }

            BigDecimal currentPrice = new BigDecimal(priceStr);
            BigDecimal quantity = BigDecimal.valueOf(h.getQuantity());

            BigDecimal pnl = currentPrice
                    .subtract(h.getAverageBuyPrice())
                    .multiply(quantity);

            totalValue = totalValue.add(currentPrice.multiply(quantity));
            totalPnL = totalPnL.add(pnl);

            positions.add(PortfolioViewDTO.Position.builder()
                    .symbol(h.getSymbol())
                    .quantity(h.getQuantity())
                    .averageBuyPrice(h.getAverageBuyPrice())
                    .currentPrice(currentPrice)
                    .pnl(pnl)
                    .build());
        }

        return PortfolioViewDTO.builder()
                .cash(portfolio.getCashBalance())
                .totalValue(totalValue)
                .totalPnL(totalPnL)
                .positions(positions)
                .build();
    }

    /**
     * Creates a portfolio in BOTH Redis and Mongo.
     */
    private RedisPortfolio createAndPersistEmptyPortfolio(String userId) {
        log.info("Creating new default portfolio for user: {}", userId);

        // ----- REDIS (The Hot Cache) -----
        RedisPortfolio redisPortfolio = new RedisPortfolio();
        redisPortfolio.setUserId(userId);
        redisPortfolio.setPortfolioName("Default Portfolio");
        redisPortfolio.setCashBalance(new BigDecimal("10000"));
        redisPortfolio.setHoldings(new ArrayList<>());
        redisPortfolio.setLastUpdated(Instant.now());

        redisPortfolioRepository.save(redisPortfolio);

        // ----- MONGO (The Cold Storage) -----
        MongoPortfolio mongoPortfolio = new MongoPortfolio();
        mongoPortfolio.setUserId(userId); // userId is the @Id now
        mongoPortfolio.setPortfolioName("Default Portfolio");
        mongoPortfolio.setCashBalance(new BigDecimal("10000"));
        mongoPortfolio.setLastUpdated(Instant.now());
        mongoPortfolio.setHoldings(new ArrayList<>());

        // Subset Pattern Initialization
        mongoPortfolio.setRecentTransactions(new ArrayList<>());
        mongoPortfolio.setPastTransactionIds(new ArrayList<>());

        mongoPortfolioRepository.save(mongoPortfolio);

        return redisPortfolio;
    }
}