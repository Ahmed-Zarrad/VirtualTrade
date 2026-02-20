package lsmsdb.unipi.it.virtualtrade.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("portfolios") // The Redis key will automatically become "portfolios:{userId}"
public class RedisPortfolio {

    @Id
    private String userId;

    private String portfolioName;

    private BigDecimal cashBalance;

    private Instant lastUpdated;

    private List<Holding> holdings = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Holding {
        private String symbol;
        private Integer quantity;
        private BigDecimal averageBuyPrice;
    }
}