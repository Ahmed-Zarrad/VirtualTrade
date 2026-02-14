package lsmsdb.unipi.it.virtualtrade.repository;

import lsmsdb.unipi.it.virtualtrade.model.StockMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockMetadataRepository extends MongoRepository<StockMetadata, String> {
    long countBySector(String sector);
}
