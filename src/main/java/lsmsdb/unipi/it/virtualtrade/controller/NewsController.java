package lsmsdb.unipi.it.virtualtrade.controller;

import lsmsdb.unipi.it.virtualtrade.dto.NewsSentimentDTO;
import lsmsdb.unipi.it.virtualtrade.model.NewsArticle;
import lsmsdb.unipi.it.virtualtrade.service.NewsArticleService;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsArticleService newsService;

    public NewsController(NewsArticleService newsService) {
        this.newsService = newsService;
    }

    /**
     * GET /api/news
     * Returns paginated latest news.
     */
    @GetMapping
    public Page<NewsArticle> getLatestNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return newsService.getLatestNews(page, size);
    }

    /**
     * GET /api/news/symbol/{symbol}
     * Returns news for a specific stock.
     */
    @GetMapping("/symbol/{symbol}")
    public Page<NewsArticle> getNewsBySymbol(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return newsService.getNewsBySymbol(symbol, page, size);
    }

    /**
     * GET /api/news/source/{source}
     * Returns news filtered by source.
     */
    @GetMapping("/source/{source}")
    public Page<NewsArticle> getNewsBySource(
            @PathVariable String source,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return newsService.getNewsBySource(source, page, size);
    }

    /**
     * GET /api/news/top-sentiment
     * Returns top N stocks ranked by news sentiment
     * in the last X days.
     *
     * Example:
     * /api/news/top-sentiment?days=7&limit=5
     */
    @GetMapping("/top-sentiment")
    public List<NewsSentimentDTO> getTopStocksBySentiment(
            @RequestParam(defaultValue = "7") int days,
            @RequestParam(defaultValue = "5") int limit
    ) {
        return newsService.getTopStocksBySentiment(days, limit);
    }
}

