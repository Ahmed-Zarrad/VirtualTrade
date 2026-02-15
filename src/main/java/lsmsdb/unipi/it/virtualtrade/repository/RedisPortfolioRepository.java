package lsmsdb.unipi.it.virtualtrade.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import lsmsdb.unipi.it.virtualtrade.model.RedisPortfolio;

@Repository
public interface RedisPortfolioRepository extends CrudRepository<RedisPortfolio, String> {

    RedisPortfolio findByUserId(String userId);
}

