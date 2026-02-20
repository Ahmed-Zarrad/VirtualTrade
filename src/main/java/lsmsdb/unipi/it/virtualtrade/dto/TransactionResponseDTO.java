package lsmsdb.unipi.it.virtualtrade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {

    private String id; // Added because the user can buy and sell the same stock multiple times
    private String symbol;
    private String type;
    private int quantity;
    private BigDecimal executionPrice;
    private Instant timestamp;
}