package lsmsdb.unipi.it.virtualtrade.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;


    @Indexed
    private String portfolioId;

    @Indexed
    private String symbol;

    private TransactionType type;

    private Integer quantity;

    private BigDecimal executionPrice;

    private Instant timestamp = Instant.now();


    public enum TransactionType {
        BUY, SELL
    }
}
