package lsmsdb.unipi.it.virtualtrade.dto;
import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;

@Getter
public class PortfolioViewDTO {

    private String userId;
    private String portfolioId;
    private BigDecimal cash;
    private BigDecimal totalValue;
    private BigDecimal totalPnL;

    private List<Position> positions;

    public PortfolioViewDTO(String userId,
                            String portfolioId,
                            BigDecimal cash,
                         BigDecimal totalValue,
                         BigDecimal totalPnL,
                         List<Position> positions) {
        this.userId = userId;
        this.portfolioId = portfolioId;
        this.cash = cash;
        this.totalValue = totalValue;
        this.totalPnL = totalPnL;
        this.positions = positions;
    }
    @Getter
    public static class Position {

        private String symbol;
        private int quantity;
        private BigDecimal averageBuyPrice;
        private BigDecimal currentPrice;
        private BigDecimal pnl;

        public Position(String symbol,
                        int quantity,
                        BigDecimal averageBuyPrice,
                        BigDecimal currentPrice,
                        BigDecimal pnl) {
            this.symbol = symbol;
            this.quantity = quantity;
            this.averageBuyPrice = averageBuyPrice;
            this.currentPrice = currentPrice;
            this.pnl = pnl;
        }
    }
}

