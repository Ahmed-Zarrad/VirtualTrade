package lsmsdb.unipi.it.virtualtrade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewsSentimentDTO {

    private String symbol;
    private Integer score;
}
