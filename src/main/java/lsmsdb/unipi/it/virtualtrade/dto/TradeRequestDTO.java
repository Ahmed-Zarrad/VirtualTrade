package lsmsdb.unipi.it.virtualtrade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeRequestDTO {

    private String userId; // Fixed the capital 'U' to prevent JSON mapping crashes
    private String symbol;
    private int quantity;

}