package lsmsdb.unipi.it.virtualtrade.service;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;


import lsmsdb.unipi.it.virtualtrade.model.RedisPortfolio;
import lsmsdb.unipi.it.virtualtrade.repository.RedisPortfolioRepository;
import lsmsdb.unipi.it.virtualtrade.dto.PortfolioViewDTO;


@Service
public class PortfolioService {

    private final RedisPortfolioRepository redisPortfolioRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public PortfolioService(RedisPortfolioRepository redisPortfolioRepository,
                            RedisTemplate<String, String> redisTemplate) {
        this.redisPortfolioRepository = redisPortfolioRepository;
        this.redisTemplate = redisTemplate;
    }

    public PortfolioViewDTO getPortfolioView(String userId) {

        RedisPortfolio portfolio = redisPortfolioRepository.findById(userId)
                .orElseThrow();

        BigDecimal totalValue = portfolio.getCashBalance();
        BigDecimal totalPnL = BigDecimal.ZERO;

        List<PortfolioViewDTO.Position> positions = new ArrayList<>();

        Map<Object, Object> prices = redisTemplate.opsForHash().entries("prices");

        for (RedisPortfolio.Holding h : portfolio.getHoldings()) {

            String priceStr = (String) prices.get(h.getSymbol());
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


    private RedisPortfolio createEmptyPortfolio(String userId) {
        RedisPortfolio portfolio = new RedisPortfolio();
        portfolio.setUserId(userId);
        portfolio.setCashBalance(new BigDecimal("10000")); // initial
        portfolio.setHoldings(new ArrayList<>());
        portfolio.setLastUpdated(Instant.now());
        return portfolio;
    }
}

