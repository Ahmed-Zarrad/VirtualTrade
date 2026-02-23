package lsmsdb.unipi.it.virtualtrade.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockPriceCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String HASH_KEY = "prices";

    public void cachePrice(String symbol, Double price) {
        redisTemplate.opsForHash().put(HASH_KEY, symbol, price.toString());
    }

    public Double getCachedPrice(String symbol) {
        Object price = redisTemplate.opsForHash().get(HASH_KEY, symbol);
        if (price != null) {
            return Double.valueOf(price.toString());
        }
        return null;
    }
}