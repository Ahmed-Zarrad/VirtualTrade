package lsmsdb.unipi.it.virtualtrade.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "portfolios")
public class MongoPortfolio {

    @Id
    private String userId;

    private String portfolioName;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal cashBalance;

    private Instant lastUpdated;

    private List<Holding> holdings = new ArrayList<>();

    // --- THE SUBSET PATTERN IMPLEMENTATION ---

    // 1. The Bounded Array: Stores only the 10 most recent transactions for fast UI loading
    private List<Transaction> recentTransactions = new ArrayList<>();

    // 2. The Document Links: Stores the IDs of all transactions older than the most recent 10
    private List<String> pastTransactionIds = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Holding {
        private String symbol;
        private Integer quantity;

        @Field(targetType = FieldType.DECIMAL128)
        private BigDecimal averageBuyPrice;
    }
}