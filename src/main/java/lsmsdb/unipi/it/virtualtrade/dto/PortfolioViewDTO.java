package lsmsdb.unipi.it.virtualtrade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioViewDTO {

    private BigDecimal cash;

    // Total Value = cashBalance + (sum of all position current values)
    private BigDecimal totalValue;

    // Overall Profit/Loss across the entire portfolio
    private BigDecimal totalPnL;

    private List<Position> positions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Position {
        private String symbol;
        private int quantity;
        private BigDecimal averageBuyPrice;

        // This will be injected dynamically from the Redis Live Cache
        private BigDecimal currentPrice;

        // Dynamically calculated: (currentPrice - averageBuyPrice) * quantity
        private BigDecimal pnl;
    }
}