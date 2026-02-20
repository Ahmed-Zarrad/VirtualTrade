package lsmsdb.unipi.it.virtualtrade;

import com.fasterxml.jackson.databind.ObjectMapper;
import lsmsdb.unipi.it.virtualtrade.model.NewsArticle;
import lsmsdb.unipi.it.virtualtrade.repository.NewsArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class NewsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NewsArticleRepository newsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDb() {
        newsRepository.deleteAll();
    }

    @Test
    void fullNewsFlowTest() throws Exception {

        Instant now = Instant.now();

        // 🔹 Insert 15 news
        for (int i = 0; i < 15; i++) {

            NewsArticle article = new NewsArticle();
            article.setTitle("News " + i);
            article.setDescription("Description " + i);
            article.setSource(i % 2 == 0 ? "Reuters" : "Bloomberg");
            article.setUrl("http://test.com/" + i);
            article.setUrlToImage("http://img.com/" + i);
            article.setSummary("Summary " + i);
            article.setPublishedAt(now.minus(i, ChronoUnit.HOURS));

            // Alternate symbols
            if (i % 2 == 0) {
                article.setRelatedSymbols(List.of("AAPL"));
                article.setSentiment(NewsArticle.Sentiment.POSITIVE);
            } else {
                article.setRelatedSymbols(List.of("TSLA"));
                article.setSentiment(NewsArticle.Sentiment.NEGATIVE);
            }

            newsRepository.save(article);
        }

        // 🔹 1️⃣ Test latest news pagination
        mockMvc.perform(get("/api/news")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(10));

        // 🔹 2️⃣ Test filter by symbol
        mockMvc.perform(get("/api/news/symbol/AAPL")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].relatedSymbols[0]").value("AAPL"));

        // 🔹 3️⃣ Test filter by source
        mockMvc.perform(get("/api/news/source/Reuters")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].source").value("Reuters"));

        // 🔹 4️⃣ Test sentiment ranking
        mockMvc.perform(get("/api/news/top-sentiment")
                        .param("days", "7")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].symbol").exists())
                .andExpect(jsonPath("$[0].score").exists());
    }
}



