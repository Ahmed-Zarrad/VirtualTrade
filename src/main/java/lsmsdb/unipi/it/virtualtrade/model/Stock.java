package lsmsdb.unipi.it.virtualtrade.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stocks") // Separate collection for high-speed access
public class Stock {

    @Id
    private String symbol; // e.g., "AAPL" (Links to StockMetadata)

    private Double currentPrice;
    private Double changePercent;


    @Builder.Default
    private List<DailyCandle> dailyHistory = new ArrayList<>();

    @LastModifiedDate
    private Instant lastUpdated;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailyCandle {
        private LocalDate date;
        private Double open;
        private Double high;
        private Double low;
        private Double close;
        private Long volume;
    }
}