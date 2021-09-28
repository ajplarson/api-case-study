package com.example.demo.repos;

import com.example.demo.models.Price;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceRepository extends MongoRepository<Price, Long> {
  public Optional<Price> findById(Long id);

  public Optional<List<Price>> findByValue(BigDecimal value);

}
