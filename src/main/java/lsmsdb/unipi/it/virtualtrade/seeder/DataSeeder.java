package lsmsdb.unipi.it.virtualtrade.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lsmsdb.unipi.it.virtualtrade.model.StockMetadata;
import lsmsdb.unipi.it.virtualtrade.repository.StockMetadataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final StockMetadataRepository stockMetadataRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("Checking if database needs to be seeded...");

        // Only seed if MongoDB is empty so we don't create duplicates on every restart
        if (stockMetadataRepository.count() == 0) {
            log.info("MongoDB is empty. Seeding initial Market Data...");

            // 1. Seed MongoDB (Cold Storage) with Stock Metadata
            List<StockMetadata> stocks = List.of(
                    StockMetadata.builder().symbol("AAPL").companyName("Apple Inc.").sector("Technology").build(),
                    StockMetadata.builder().symbol("TSLA").companyName("Tesla Inc.").sector("Automotive").build(),
                    StockMetadata.builder().symbol("GOOGL").companyName("Alphabet Inc.").sector("Technology").build(),
                    StockMetadata.builder().symbol("AMZN").companyName("Amazon.com Inc.").sector("Consumer Cyclical").build(),
                    StockMetadata.builder().symbol("MSFT").companyName("Microsoft Corp.").sector("Technology").build()
            );
            stockMetadataRepository.saveAll(stocks);

            // 2. Seed Redis (Hot Cache) with initial stock prices
            log.info("Seeding Redis Hot Cache with initial prices...");
            redisTemplate.opsForHash().put("prices", "AAPL", "175.50");
            redisTemplate.opsForHash().put("prices", "TSLA", "210.25");
            redisTemplate.opsForHash().put("prices", "GOOGL", "140.00");
            redisTemplate.opsForHash().put("prices", "AMZN", "155.80");
            redisTemplate.opsForHash().put("prices", "MSFT", "405.10");

            log.info("Database Seeding Complete! VirtualTrade is ready to execute trades.");
        } else {
            log.info("Database already contains data. Skipping seeder.");
        }
    }
}