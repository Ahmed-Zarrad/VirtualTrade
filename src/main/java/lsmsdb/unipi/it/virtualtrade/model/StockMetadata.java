package lsmsdb.unipi.it.virtualtrade.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "stocks_metadata")
public class StockMetadata {
    @Id
    private String symbol;

    private String companyName;

    @Indexed
    private String sector;

    private String industry;
}
