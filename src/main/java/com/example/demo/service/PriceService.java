package com.example.demo.service;

import com.example.demo.models.Price;
import com.example.demo.repos.PriceRepository;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService {

  private final PriceRepository repo;

  public Price addPrice(Long id, BigDecimal value, Currency currencyCode) {
    Price price = Price.builder().id(id).value(value).currencyCode(currencyCode).build();
    return repo.save(price);
  }

  public List<Price> getAllPrices() {
    return repo.findAll();
  }

  public Optional<Price> getPriceById(Long id) {
    return repo.findById(id);
  }

  public Optional<Price> updatePriceById(Long id, BigDecimal desiredValue) {
    Optional<Price> originalPrice = repo.findById(id);
    if (originalPrice.isEmpty()) {
      return Optional.empty();
    }
    Price updatedPrice =
        Price.builder()
            .id(originalPrice.get().getId())
            .value(desiredValue)
            .currencyCode(originalPrice.get().getCurrencyCode())
            .build();
    return Optional.of(repo.save(updatedPrice));
  }
}
