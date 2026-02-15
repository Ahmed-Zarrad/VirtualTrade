package lsmsdb.unipi.it.virtualtrade.dto;
import java.math.BigDecimal;
import java.util.List;

public class PortfolioViewDTO {

    private BigDecimal cash;
    private BigDecimal totalValue;
    private BigDecimal totalPnL;

    private List<Position> positions;

    public PortfolioViewDTO(BigDecimal cash,
                         BigDecimal totalValue,
                         BigDecimal totalPnL,
                         List<Position> positions) {
        this.cash = cash;
        this.totalValue = totalValue;
        this.totalPnL = totalPnL;
        this.positions = positions;
    }

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

