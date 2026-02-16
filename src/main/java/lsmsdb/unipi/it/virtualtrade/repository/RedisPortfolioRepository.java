package lsmsdb.unipi.it.virtualtrade.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import lsmsdb.unipi.it.virtualtrade.model.RedisPortfolio;
import java.util.Optional;

@Repository
public interface RedisPortfolioRepository extends CrudRepository<RedisPortfolio, String> {

    Optional<RedisPortfolio> findByUserId(String userId);
}

