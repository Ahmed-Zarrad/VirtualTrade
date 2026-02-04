package lsmsdb.unipi.it.virtualtrade.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@CompoundIndexes({
        @CompoundIndex(name = "symbol_date_hour_idx", def = "{'symbol': 1, 'date': 1, 'hour': 1}", unique = true)
})
@Document(collection = "market_data_buckets")
public class MarketDataBucket {

    @Id
    private String id;

    private String symbol;

    private LocalDate date;

    private Integer hour;

    private List<PriceTick> ticks = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PriceTick {
        private int m;
        private BigDecimal o;
        private BigDecimal h;
        private BigDecimal l;
        private BigDecimal c;
        private long v;
    }
}
