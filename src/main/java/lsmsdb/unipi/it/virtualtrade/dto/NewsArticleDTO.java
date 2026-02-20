package lsmsdb.unipi.it.virtualtrade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsArticleDTO {

    private String id; // Added so the frontend has a unique key for mapping lists!
    private String title;
    private String description;
    private String source;
    private String url;
    private String urlToImage;
    private String summary;
    private Instant publishedAt;

    // Kept as String to avoid frontend JSON deserialization errors. Perfect choice!
    private String sentiment;

    private List<String> relatedSymbols;
}