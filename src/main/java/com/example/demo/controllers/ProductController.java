package com.example.demo.controllers;

import com.example.demo.models.Price;
import com.example.demo.models.PriceRequestObject;
import com.example.demo.models.Product;
import com.example.demo.service.ApiService;
import com.example.demo.service.PriceService;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

// TO-DO:
// Add swagger / open API docs
// Add pre-authorize on controller

@RestController
@RequiredArgsConstructor
public class ProductController {

  // service that ties together methods from other services
  private final ApiService apiService;

  // we need this service to create a price for demo and update
  private final PriceService priceService;

  // Get the product (if possible) for a specified ID
  @GetMapping("/api/product/{productId}")
  public Product getProductById(@PathVariable(name = "productId") String productId) {
      Optional<Product> product = apiService.populateProductObject(productId);
      if (product.isPresent()) {
        return product.get();
      } else {
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, String.format("Product ID %s not found", productId));
      }
  }

  // Update the product (if possible) for a specified ID
  @PutMapping("/api/product/{productId}")
  public Product updateProductById(
      @PathVariable(name = "productId") String productId,
      @RequestBody PriceRequestObject requestedPrice) {
      Optional<Product> product = apiService.populateProductObject(productId);
      //if product is present then price has to be valid
      if (product.isPresent()) {
        Product updatedProduct = product.get();
        Long requestedId = Long.valueOf(requestedPrice.getId());
        BigDecimal requestedValue = BigDecimal.valueOf(Double.valueOf(requestedPrice.getValue()));
        Price updatedPrice = priceService.updatePriceById(requestedId, requestedValue).get();
        updatedProduct.setPrice(updatedPrice);
        return updatedProduct;
      } else {
        throw new ResponseStatusException(
            HttpStatus.NOT_FOUND, String.format("Product ID %s not found", productId));
      }
  }

  // Post the price so we don't have to populate the DB manually
  // for convenience, i am assigning the same id to products and prices
  @PostMapping("/api/price")
  public Price createPrice(@RequestBody PriceRequestObject requestedPrice) {
    Long requestedId = Long.valueOf(requestedPrice.getId());
    BigDecimal requestedValue = BigDecimal.valueOf(Double.valueOf(requestedPrice.getValue()));
    Currency requestedCurrencyCode = Currency.getInstance(requestedPrice.getCurrencyCode());
    return priceService.addPrice(requestedId, requestedValue, requestedCurrencyCode);
  }
}
