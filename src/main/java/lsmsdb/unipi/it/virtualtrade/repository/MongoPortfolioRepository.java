package lsmsdb.unipi.it.virtualtrade.repository;

import lsmsdb.unipi.it.virtualtrade.model.MongoPortfolio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoPortfolioRepository extends MongoRepository<MongoPortfolio, String> {

    // we don't need any custom methods here right now
    // To get the portfolio, the Service layer will just call:
    // mongoPortfolioRepository.findById(userId);

}