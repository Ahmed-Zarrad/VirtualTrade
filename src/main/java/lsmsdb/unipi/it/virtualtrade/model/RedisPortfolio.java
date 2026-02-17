package lsmsdb.unipi.it.virtualtrade.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.redis.core.RedisHash;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

import java.math.BigDecimal;

@RedisHash("portfolios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisPortfolio {

    @Id
    private String userId;

    private String portfolioId;

    private String portfolioName;

    private BigDecimal cashBalance;

    private Instant lastUpdated;

    private List<Holding>  holdings = new ArrayList<>();
    private BigDecimal currentPrice;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Holding {
        private String symbol;
        private Integer quantity;
        private BigDecimal averageBuyPrice;
    }
}

