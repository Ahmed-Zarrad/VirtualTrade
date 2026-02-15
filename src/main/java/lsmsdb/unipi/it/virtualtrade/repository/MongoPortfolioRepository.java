package lsmsdb.unipi.it.virtualtrade.repository;

import lsmsdb.unipi.it.virtualtrade.model.MongoPortfolio;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MongoPortfolioRepository extends MongoRepository<MongoPortfolio, String> {
    List<MongoPortfolio> findByUserId(String userId);
}