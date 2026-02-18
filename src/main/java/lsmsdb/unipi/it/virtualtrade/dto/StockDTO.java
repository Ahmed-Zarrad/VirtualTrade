package lsmsdb.unipi.it.virtualtrade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {

    private String symbol;
    private Double currentPrice;
    private Double changePercent;

    // The chart data sent to the frontend
    private List<DailyCandleDTO> dailyHistory;

    private Instant lastUpdated;

    // --- Inner DTO for the Candle Data ---
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyCandleDTO {
        private LocalDate date;
        private Double open;
        private Double high;
        private Double low;
        private Double close;
        private Long volume;
    }
}