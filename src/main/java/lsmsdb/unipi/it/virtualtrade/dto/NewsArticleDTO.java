package lsmsdb.unipi.it.virtualtrade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class NewsArticleDTO {

    private String title;
    private String description;
    private String source;
    private String url;
    private String urlToImage;
    private String summary;
    private Instant publishedAt;
    private String sentiment;      // String, non enum
    private List<String> relatedSymbols;
}




