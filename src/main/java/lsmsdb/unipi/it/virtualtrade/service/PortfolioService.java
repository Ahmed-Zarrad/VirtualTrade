package lsmsdb.unipi.it.virtualtrade.service;

import lsmsdb.unipi.it.virtualtrade.dto.PortfolioViewDTO;
import lsmsdb.unipi.it.virtualtrade.model.RedisPortfolio;
import lsmsdb.unipi.it.virtualtrade.model.MongoPortfolio;
import lsmsdb.unipi.it.virtualtrade.repository.RedisPortfolioRepository;
import lsmsdb.unipi.it.virtualtrade.repository.MongoPortfolioRepository;

import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
public class PortfolioService {

    private final RedisPortfolioRepository redisPortfolioRepository;
    private final MongoPortfolioRepository mongoPortfolioRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public PortfolioService(RedisPortfolioRepository redisPortfolioRepository,
                            MongoPortfolioRepository mongoPortfolioRepository,
                            RedisTemplate<String, String> redisTemplate) {
        this.redisPortfolioRepository = redisPortfolioRepository;
        this.mongoPortfolioRepository = mongoPortfolioRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * Returns portfolio view for a given user.
     * If portfolio does not exist, it creates one in BOTH Redis and Mongo.
     */
    public PortfolioViewDTO getPortfolioView(String userId) {

        // Try to find portfolio in Redis by userId
        RedisPortfolio portfolio = redisPortfolioRepository
                .findByUserId(userId)
                .orElseGet(() -> createAndPersistEmptyPortfolio(userId));

        BigDecimal totalValue = portfolio.getCashBalance();
        BigDecimal totalPnL = BigDecimal.ZERO;

        List<PortfolioViewDTO.Position> positions = new ArrayList<>();

        // Get all latest prices from Redis (single access)
        Map<Object, Object> prices = redisTemplate.opsForHash().entries("prices");

        for (RedisPortfolio.Holding h : portfolio.getHoldings()) {

            String priceStr = (String) prices.get(h.getSymbol());

            if (priceStr == null) continue;

            BigDecimal currentPrice = new BigDecimal(priceStr);
            BigDecimal quantity = BigDecimal.valueOf(h.getQuantity());

            BigDecimal pnl = currentPrice
                    .subtract(h.getAverageBuyPrice())
                    .multiply(quantity);

            totalValue = totalValue.add(currentPrice.multiply(quantity));
            totalPnL = totalPnL.add(pnl);

            positions.add(new PortfolioViewDTO.Position(
                    h.getSymbol(),
                    h.getQuantity(),
                    h.getAverageBuyPrice(),
                    currentPrice,
                    pnl
            ));
        }

        return new PortfolioViewDTO(
                portfolio.getCashBalance(),
                totalValue,
                totalPnL,
                positions
        );
    }

    /**
     * Creates a portfolio in BOTH Redis and Mongo.
     */
    private RedisPortfolio createAndPersistEmptyPortfolio(String userId) {

        String portfolioId = UUID.randomUUID().toString();

        // ----- REDIS -----
        RedisPortfolio redisPortfolio = new RedisPortfolio();
        redisPortfolio.setId(portfolioId);
        redisPortfolio.setUserId(userId);
        redisPortfolio.setPortfolioName("Default Portfolio");
        redisPortfolio.setCashBalance(new BigDecimal("10000"));
        redisPortfolio.setHoldings(new ArrayList<>());
        redisPortfolio.setLastUpdated(Instant.now());

        redisPortfolioRepository.save(redisPortfolio);

        // ----- MONGO -----
        MongoPortfolio mongoPortfolio = new MongoPortfolio();
        mongoPortfolio.setId(portfolioId);
        mongoPortfolio.setUserId(userId);
        mongoPortfolio.setPortfolioName("Default Portfolio");
        mongoPortfolio.setRecentTransactions(new ArrayList<>());

        mongoPortfolioRepository.save(mongoPortfolio);

        return redisPortfolio;
    }
}
