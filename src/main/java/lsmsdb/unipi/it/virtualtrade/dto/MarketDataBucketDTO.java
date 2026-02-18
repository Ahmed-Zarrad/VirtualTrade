package lsmsdb.unipi.it.virtualtrade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketDataBucketDTO {

    private String symbol;
    private LocalDate date;
    private List<PriceTickDTO> ticks;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceTickDTO {
        private int m;
        private Double o;
        private Double h;
        private Double l;
        private Double c;
        private Long v;
    }
}