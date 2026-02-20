package lsmsdb.unipi.it.virtualtrade.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMetadataDTO {

    @NotBlank(message = "Symbol is required")
    private String symbol;

    @NotBlank(message = "Company name is required")
    private String companyName;

    private String sector;
    private String industry;
    private String description;
    private Double marketCap;
    private Double dividendYield;
}