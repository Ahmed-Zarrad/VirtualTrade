package lsmsdb.unipi.it.virtualtrade.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "market_data_buckets")
@CompoundIndexes({
        @CompoundIndex(name = "stock_date_idx", def = "{'symbol': 1, 'date': 1}", unique = true)
})
public class MarketDataBucket {

    @Id
    private String id;

    private String symbol;
    private LocalDate date;

    @Builder.Default
    private List<PriceTick> ticks = new ArrayList<>();


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PriceTick {
        private int m;
        private Double o;
        private Double h;
        private Double l;
        private Double c;
        private Long v;
    }
}