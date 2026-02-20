package lsmsdb.unipi.it.virtualtrade.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "news_articles")
public class NewsArticle {

    @Id
    private String id;

    // MongoDB Multikey Index: Instantly finds all news containing a specific stock symbol
    @Indexed
    private List<String> relatedSymbols;

    // Adding TextIndexed with a higher weight so title matches rank higher in search results
    @TextIndexed(weight = 2)
    private String title;

    private String description;

    private String source;

    private String url;

    private String urlToImage;

    // Standard text index for the body/summary
    @TextIndexed
    private String summary;

    // CRITICAL FIX: Ensures lightning-fast queries when sorting by "Latest News First"
    @Indexed(direction = IndexDirection.DESCENDING)
    private Instant publishedAt;

    private Sentiment sentiment;

    public enum Sentiment {
        POSITIVE, NEGATIVE, NEUTRAL
    }
}