package lsmsdb.unipi.it.virtualtrade.repository;

import lsmsdb.unipi.it.virtualtrade.model.StockMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;

public interface StockMetadataRepository extends MongoRepository<StockMetadata, String> {
    long countBySector(String sector);
    Optional<StockMetadata> findBySymbol(String symbol);
    List<StockMetadata> findBySector(String sector);
    Boolean existsBySymbol(String symbol);
}
