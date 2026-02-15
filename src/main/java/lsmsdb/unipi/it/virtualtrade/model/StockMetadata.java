package lsmsdb.unipi.it.virtualtrade.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document(collection = "stocksMetadata")
public class StockMetadata{
    @Id
    private String symbol;

    private String companyName;

    @Indexed
    private String sector;

    private String industry;

    private List<DailySummary> dailySummaries = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DailySummary{

        private LocalDate date;

        private BigDecimal open;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal close;
        private Long volume;

        private String bucketId; // reference to detailed bucket document
    }
}
