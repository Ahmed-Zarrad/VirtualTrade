package lsmsdb.unipi.it.virtualtrade.service;

import lsmsdb.unipi.it.virtualtrade.dto.NewsArticleDTO;
import lsmsdb.unipi.it.virtualtrade.dto.NewsSentimentDTO;
import lsmsdb.unipi.it.virtualtrade.model.NewsArticle;
import lsmsdb.unipi.it.virtualtrade.repository.NewsArticleRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NewsArticleService {

    private final NewsArticleRepository newsRepository;

    public NewsArticleService(NewsArticleRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    /**
     * Returns latest global news (paginated).
     */
    public Page<NewsArticleDTO> getLatestNews(int page, int size) {
        Page<NewsArticle> articles =
                newsRepository.findAllByOrderByPublishedAtDesc(
                PageRequest.of(page, size)
        );
        return articles.map(this::toDTO);
    }

    /**
     * Returns latest news for a specific stock symbol.
     */
    public Page<NewsArticleDTO> getNewsBySymbol(
            String symbol,
            int page,
            int size
    ) {
        Page<NewsArticle> articles =
                newsRepository.findByRelatedSymbolsContainingOrderByPublishedAtDesc(
                        symbol,
                        PageRequest.of(page, size)
                );
        return articles.map(this::toDTO);

    }

    /**
     * Returns latest news filtered by source.
     */
    public Page<NewsArticleDTO> getNewsBySource(
            String source,
            int page,
            int size
    ) {
        Page<NewsArticle> articles =
                newsRepository.findBySourceOrderByPublishedAtDesc(
                        source,
                        PageRequest.of(page, size)
                );
        return articles.map(this::toDTO);
    }

    /**
     * Returns top N stocks ranked by news sentiment
     * within the last X days.
     */
    public List<NewsSentimentDTO> getTopStocksBySentiment(
            int days,
            int limit
    ) {

        Instant end = Instant.now();
        Instant start = end.minus(days, ChronoUnit.DAYS);

        return newsRepository.findTopStocksBySentimentBetween(
                start,
                end,
                limit
        );
    }

    private NewsArticleDTO toDTO(NewsArticle article) {
        return new NewsArticleDTO(
                article.getTitle(),
                article.getDescription(),
                article.getSource(),
                article.getUrl(),
                article.getUrlToImage(),
                article.getSummary(),
                article.getPublishedAt(),
                article.getSentiment() != null
                        ? article.getSentiment().name()
                        : null,
                article.getRelatedSymbols()
        );
    }
}


