package lsmsdb.unipi.it.virtualtrade.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradeRequestDTO {

    private String UserId;
    private String symbol;
    private int quantity;
}
