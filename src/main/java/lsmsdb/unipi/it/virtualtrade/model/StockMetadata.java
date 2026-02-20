package lsmsdb.unipi.it.virtualtrade.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stocks_metadata")
public class StockMetadata {

    @Id
    private String symbol;

    private String companyName;

    @Indexed
    private String sector;

    private String industry;

    private String description;
    private Double marketCap;
    private Double dividendYield;

    @LastModifiedDate
    private Instant lastUpdated;
}