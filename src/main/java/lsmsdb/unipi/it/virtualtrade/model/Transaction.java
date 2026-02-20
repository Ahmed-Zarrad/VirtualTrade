package lsmsdb.unipi.it.virtualtrade.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id; // This is the ID that will be pushed to the pastTransactionIds array!

    @Indexed
    private String userId;

    @Indexed
    private String symbol;

    private TransactionType type;

    private Integer quantity;

    // Forces MongoDB to use the precise Decimal128 format for money
    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal executionPrice;

    // Indexed so the repository can quickly sort history by newest first
    @Indexed
    private Instant timestamp;

    public enum TransactionType {
        BUY, SELL
    }
}