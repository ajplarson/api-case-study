package com.example.demo.repos;

import com.example.demo.models.Price;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PriceRepository extends MongoRepository<Price, Long> {
    public Optional<Price> findById(Long id);
    public Optional<List<Price>> findByValue(BigDecimal value);
}
