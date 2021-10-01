package com.example.demo.service;

import com.example.demo.models.Price;
import com.example.demo.models.Product;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiService {
  private final PriceService priceService;
  private final ProductService productService;

  public Optional<Product> populateProductObject(String productId) {
    Optional<Price> productPrice = priceService.getPriceById(Long.valueOf(productId));
    Optional<String> productName = productService.getProductNameFromResponse(productId);
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
