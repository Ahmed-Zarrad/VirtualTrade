package lsmsdb.unipi.it.virtualtrade.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StockPriceCacheService {

    private final RedisTemplate<String, Object> redisTemplate;


    private static final String KEY_PREFIX = "stock:price:";


    public void cachePrice(String symbol, Double price) {
        String key = KEY_PREFIX + symbol;

        redisTemplate.opsForValue().set(key, price, 5, TimeUnit.MINUTES);
    }


    public Double getCachedPrice(String symbol) {
        String key = KEY_PREFIX + symbol;
        Object price = redisTemplate.opsForValue().get(key);

        if (price != null) {
            return Double.valueOf(price.toString());
        }
        return null;
    }
}