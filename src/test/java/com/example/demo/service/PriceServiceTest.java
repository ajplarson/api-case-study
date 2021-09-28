package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.example.demo.models.Price;
import com.example.demo.repos.PriceRepository;
import com.example.demo.utils.InvalidQueryException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PriceServiceTest {

  @MockBean private PriceRepository repo;

  @Autowired private PriceService priceService;

  @DisplayName("Test addPrice")
  @Test
  void testAddPrice() {
    // given
    Long sampleId = Long.valueOf("123");
    BigDecimal sampleValue = BigDecimal.TEN;
    Currency sampleCurrency = Currency.getInstance("USD");
    Price samplePrice =
        Price.builder().id(sampleId).value(sampleValue).currencyCode(sampleCurrency).build();
    when(repo.save(Mockito.any(Price.class))).thenReturn(samplePrice);

    // when
    var actualPrice = priceService.addPrice(sampleId, sampleValue, sampleCurrency);

    // then
    assertEquals(actualPrice.getId(), sampleId);
    assertEquals(actualPrice.getValue(), sampleValue);
    assertEquals(actualPrice.getCurrencyCode(), sampleCurrency);
  }

  @DisplayName("Test testFindPriceByIdHappy")
  @Test
  void testFindPriceByIdHappy() throws InvalidQueryException {
    // given
    Long sampleId = Long.valueOf("123");
    BigDecimal sampleValue = BigDecimal.TEN;
    Currency sampleCurrency = Currency.getInstance("USD");
    Price samplePrice =
        Price.builder().id(sampleId).value(sampleValue).currencyCode(sampleCurrency).build();
    when(repo.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(samplePrice));

    // when
    var actualPrice = priceService.getPriceById(sampleId);

    // then
    assertEquals(actualPrice.getId(), sampleId);
    assertEquals(actualPrice.getValue(), sampleValue);
    assertEquals(actualPrice.getCurrencyCode(), sampleCurrency);
  }

  @DisplayName("Test testFindPriceByIdUnhappy")
  @Test
  void testFindPriceByIdUnhappy() throws InvalidQueryException {
    // given
    when(repo.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

    // then
    assertThrows(InvalidQueryException.class, () -> priceService.getPriceById(Long.valueOf("123")));
  }

  @DisplayName("Test testUpdatePriceByIdHappy")
  @Test
  void testUpdatePriceByIdHappy() throws InvalidQueryException {
    // given
    Long sampleId = Long.valueOf("123");
    BigDecimal sampleValue = BigDecimal.TEN;
    BigDecimal desiredValue = BigDecimal.ONE;
    Currency sampleCurrency = Currency.getInstance("USD");
    Price samplePrice =
        Price.builder().id(sampleId).value(sampleValue).currencyCode(sampleCurrency).build();
    Price sampleUpdatedPrice =
        Price.builder().id(sampleId).value(desiredValue).currencyCode(sampleCurrency).build();
    when(repo.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(samplePrice));
    when(repo.save(Mockito.any(Price.class))).thenReturn(sampleUpdatedPrice);

    // when
    var updatedPrice = priceService.updatePriceById(sampleId, desiredValue);

    // then
    assertEquals(updatedPrice.getId(), sampleId);
    assertEquals(updatedPrice.getValue(), desiredValue);
    assertEquals(updatedPrice.getCurrencyCode(), sampleCurrency);
  }

  @DisplayName("Test testUpdatePriceByIdUnhappy")
  @Test
  void testUpdatePriceByIdUnhappy() throws InvalidQueryException {
    // given
    when(repo.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

    // then
    assertThrows(
        InvalidQueryException.class,
        () -> priceService.updatePriceById(Long.valueOf("123"), BigDecimal.ONE));
  }
}
