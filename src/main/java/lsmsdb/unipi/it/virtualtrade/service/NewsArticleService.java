package lsmsdb.unipi.it.virtualtrade.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lsmsdb.unipi.it.virtualtrade.dto.NewsArticleDTO;
import lsmsdb.unipi.it.virtualtrade.dto.NewsSentimentDTO;
import lsmsdb.unipi.it.virtualtrade.model.NewsArticle;
import lsmsdb.unipi.it.virtualtrade.repository.NewsArticleRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor // Automatically generates the constructor!
public class NewsArticleService {

    private final NewsArticleRepository newsRepository;

    /**
     * Returns latest global news (paginated).
     */
    public Page<NewsArticleDTO> getLatestNews(int page, int size) {
        Page<NewsArticle> articles =
                newsRepository.findAllByOrderByPublishedAtDesc(PageRequest.of(page, size));
        return articles.map(this::toDTO);
    }

    /**
     * Returns latest news for a specific stock symbol.
     */
    public Page<NewsArticleDTO> getNewsBySymbol(String symbol, int page, int size) {
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
    public Page<NewsArticleDTO> getNewsBySource(String source, int page, int size) {
        Page<NewsArticle> articles =
                newsRepository.findBySourceOrderByPublishedAtDesc(
                        source,
                        PageRequest.of(page, size)
                );
        return articles.map(this::toDTO);
    }

    // --- NEW: THE FULL-TEXT SEARCH FEATURE ---

    /**
     * Performs a lightning-fast keyword search across all indexed news titles and summaries.
     */
    public Page<NewsArticleDTO> searchNews(String keyword, int page, int size) {
        TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(keyword);
        Page<NewsArticle> articles = newsRepository.findAllBy(criteria, PageRequest.of(page, size));
        return articles.map(this::toDTO);
    }

    /**
     * Returns top N stocks ranked by news sentiment within the last X days.
     */
    public List<NewsSentimentDTO> getTopStocksBySentiment(int days, int limit) {
        Instant end = Instant.now();
        Instant start = end.minus(days, ChronoUnit.DAYS);

        return newsRepository.findTopStocksBySentimentBetween(start, end, limit);
    }

    /**
     * Converts the MongoDB entity to our frontend-friendly DTO using the Builder pattern.
     */
    private NewsArticleDTO toDTO(NewsArticle article) {
        return NewsArticleDTO.builder()
                .id(article.getId()) // Critical for the frontend React/Angular UI!
                .title(article.getTitle())
                .description(article.getDescription())
                .source(article.getSource())
                .url(article.getUrl())
                .urlToImage(article.getUrlToImage())
                .summary(article.getSummary())
                .publishedAt(article.getPublishedAt())
                .sentiment(article.getSentiment() != null ? article.getSentiment().name() : null)
                .relatedSymbols(article.getRelatedSymbols())
                .build();
    }
}