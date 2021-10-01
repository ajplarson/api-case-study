package com.example.demo.service;

import com.example.demo.models.Price;
import com.example.demo.models.Product;
import com.example.demo.utils.InvalidQueryException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiService {
  private final PriceService priceService;
  private final ProductService productService;

  private Optional<Price> getPriceByProductId(String productId) {
    try {
      return Optional.of(priceService.getPriceById(Long.valueOf(productId)));
    } catch (InvalidQueryException e) {
      return Optional.empty();
    }
  }

  public Optional<Product> populateProductObject(String productId) {
    Optional<Price> productPrice = getPriceByProductId(productId);
    Optional<String> productName = productService.getProductNameFromResponse();
    if (productPrice.isPresent() && productName.isPresent()) {
      return Optional.of(
          Product.builder()
              .id(Long.valueOf(productId))
              .name(productName.get())
              .price(productPrice.get())
              .build());
    } else {
      return Optional.empty();
    }
  }
}
