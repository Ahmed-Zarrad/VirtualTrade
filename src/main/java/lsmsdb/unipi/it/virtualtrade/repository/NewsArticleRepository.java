package lsmsdb.unipi.it.virtualtrade.repository;

import lsmsdb.unipi.it.virtualtrade.model.NewsArticle;
import lsmsdb.unipi.it.virtualtrade.dto.NewsSentimentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface NewsArticleRepository extends MongoRepository<NewsArticle, String> {

    /**
     * Returns latest global news ordered by publication date (descending).
     */
    Page<NewsArticle> findAllByOrderByPublishedAtDesc(Pageable pageable);

    /**
     * Returns latest news related to a specific stock symbol.
     */
    Page<NewsArticle> findByRelatedSymbolsContainingOrderByPublishedAtDesc(
            String symbol,
            Pageable pageable
    );

    /**
     * Returns latest news filtered by source.
     */
    Page<NewsArticle> findBySourceOrderByPublishedAtDesc(
            String source,
            Pageable pageable
    );

    // --- THE UPGRADE: FULL-TEXT SEARCH ---

    /**
     * Leverages the @TextIndexed fields (Title and Summary) to perform lightning-fast
     * keyword searches across the news database.
     */
    Page<NewsArticle> findAllBy(TextCriteria textCriteria, Pageable pageable);

    /**
     * Aggregation:
     * Returns top N stocks ranked by sentiment score
     * within a given time interval.
     *
     * Score logic:
     * POSITIVE = +1
     * NEGATIVE = -1
     * NEUTRAL  = 0
     */
    @Aggregation(pipeline = {
            "{ $match: { publishedAt: { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$relatedSymbols' }",
            "{ $group: { " +
                    "_id: '$relatedSymbols', " +
                    "score: { $sum: { " +
                    "$switch: { " +
                    "branches: [" +
                    "{ case: { $eq: ['$sentiment', 'POSITIVE'] }, then: 1 }," +
                    "{ case: { $eq: ['$sentiment', 'NEGATIVE'] }, then: -1 }" +
                    "], " +
                    "default: 0 " +
                    "} " +
                    "} } " +
                    "} }",
            "{ $project: { symbol: '$_id', score: 1, _id: 0 } }",
            "{ $sort: { score: -1 } }",
            "{ $limit: ?2 }"
    })
    List<NewsSentimentDTO> findTopStocksBySentimentBetween(
            Instant start,
            Instant end,
            int limit
    );
}