package lsmsdb.unipi.it.virtualtrade.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Document(collection = "news_articles")
public class NewsArticle {
    @Id
    private String id;


    @Indexed
    private List<String> relatedSymbols;

    private String title;

    private String description;

    private String source;

    private String url;

    private String urlToImage;

    @TextIndexed
    private String summary;

    private Instant publishedAt;


    private Sentiment sentiment;

    public enum Sentiment {
        POSITIVE, NEGATIVE, NEUTRAL
    }
}
