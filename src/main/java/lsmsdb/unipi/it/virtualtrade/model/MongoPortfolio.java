package lsmsdb.unipi.it.virtualtrade.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "portfolios")
public class MongoPortfolio {
    @Id
    private String id;

    @Indexed
    private String userId;

    private String portfolioName;


    private BigDecimal cashBalance;

    private Instant lastUpdated;


    private List<Holding> holdings = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Holding {
        private String symbol;
        private Integer quantity;
        private BigDecimal averageBuyPrice;
    }

    List<Transaction> recentTransactions;


}
