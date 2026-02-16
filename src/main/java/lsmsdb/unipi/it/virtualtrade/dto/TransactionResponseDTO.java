package lsmsdb.unipi.it.virtualtrade.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {

    private String symbol;
    private String type;
    private int quantity;
    private BigDecimal executionPrice;
    private Instant timestamp;
}

