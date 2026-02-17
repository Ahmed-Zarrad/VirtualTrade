package lsmsdb.unipi.it.virtualtrade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TradingIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    void fullTradeFlowTest() throws Exception {

        String userId = "testUser";

        // 1️⃣ Trigger portfolio creation
        MvcResult portfolioResult = mockMvc.perform(
                        get("/api/portfolio/" + userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        // 2️⃣ Insert fake price into Redis
        redisTemplate.opsForHash().put("prices","AAPL","100");

        // 3️⃣ Perform BUY trade
        String buyRequest = """
                {
                    "userId": "%s",
                    "symbol": "AAPL",
                    "quantity": 1
                }
                """.formatted(userId);

        mockMvc.perform(post("/api/trading/buy")
                        .contentType("application/json")
                        .content(buyRequest))
                .andDo(print())
                .andExpect(status().isOk());

        // 4️⃣ Wait for async Mongo update
        Thread.sleep(1000);

        // 5️⃣ Check recent transactions
        mockMvc.perform(get("/api/transactions/" + userId + "/recent"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].symbol").value("AAPL"));


        mockMvc.perform(get("/api/portfolio/" + userId))
                .andDo(print());
    }
}
