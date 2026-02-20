package lsmsdb.unipi.it.virtualtrade.controller;

import lombok.RequiredArgsConstructor;
import lsmsdb.unipi.it.virtualtrade.dto.NewsArticleDTO;
import lsmsdb.unipi.it.virtualtrade.dto.NewsSentimentDTO;
// Removed the unused NewsArticle model import to keep it perfectly clean
import lsmsdb.unipi.it.virtualtrade.service.NewsArticleService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/news")
@RequiredArgsConstructor // Automatically generates the constructor for newsService!
public class NewsController {

    private final NewsArticleService newsService;

    /**
     * GET /api/news
     * Returns paginated latest news.
     */
    @GetMapping
    public ResponseEntity<Page<NewsArticleDTO>> getLatestNews(
            @RequestParam(value="page",defaultValue = "0") int page,
            @RequestParam(value="size",defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(newsService.getLatestNews(page, size));
    }

    /**
     * GET /api/news/symbol/{symbol}
     * Returns news for a specific stock.
     */
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<Page<NewsArticleDTO>> getNewsBySymbol(
            @PathVariable("symbol") String symbol,
            @RequestParam(value="page",defaultValue = "0") int page,
            @RequestParam(value="size",defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(newsService.getNewsBySymbol(symbol, page, size));
    }

    /**
     * GET /api/news/source/{source}
     * Returns news filtered by source.
     */
    @GetMapping("/source/{source}")
    public ResponseEntity<Page<NewsArticleDTO>> getNewsBySource(
            @PathVariable("source") String source,
            @RequestParam(value="page",defaultValue = "0") int page,
            @RequestParam(value="size",defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(newsService.getNewsBySource(source, page, size));
    }

    // --- NEW: THE FULL-TEXT SEARCH ENDPOINT ---

    /**
     * GET /api/news/search?keyword=xyz
     * Triggers the MongoDB @TextIndexed search for lightning-fast keyword lookups.
     */
    @GetMapping("/search")
    public ResponseEntity<Page<NewsArticleDTO>> searchNews(
            @RequestParam("keyword") String keyword,
            @RequestParam(value="page",defaultValue = "0") int page,
            @RequestParam(value="size",defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(newsService.searchNews(keyword, page, size));
    }

    /**
     * GET /api/news/top-sentiment
     * Returns top N stocks ranked by news sentiment in the last X days.
     */
    @GetMapping("/top-sentiment")
    public ResponseEntity<List<NewsSentimentDTO>> getTopStocksBySentiment(
            @RequestParam(value="days",defaultValue = "7") int days,
            @RequestParam(value="limit",defaultValue = "5") int limit
    ) {
        return ResponseEntity.ok(newsService.getTopStocksBySentiment(days, limit));
    }
}