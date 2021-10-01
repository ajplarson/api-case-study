package com.example.demo.controllers;

import static com.example.demo.utils.JsonUtils.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.models.Price;
import com.example.demo.models.PriceRequestObject;
import com.example.demo.models.Product;
import com.example.demo.service.ApiService;
import com.example.demo.service.PriceService;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ApiService apiService;

  @MockBean private PriceService priceService;

  @Test
  @DisplayName("Test createPriceHappy()")
  void testCreatePriceHappy() throws Exception {
    // given
    String requestId = "123";
    Long sampleId = Long.valueOf(requestId);

    String requestValue = "10";
    BigDecimal sampleValue = BigDecimal.valueOf(Long.valueOf(requestValue));

    String requestCurrency = "USD";
    Currency sampleCurrency = Currency.getInstance(requestCurrency);
    Price samplePrice =
        Price.builder().id(sampleId).value(sampleValue).currencyCode(sampleCurrency).build();

    PriceRequestObject request =
        PriceRequestObject.builder()
            .id(requestId)
            .value(requestValue)
            .currencyCode(requestCurrency)
            .build();

    // when
    mockMvc
        .perform(post("/api/price").contentType(APPLICATION_JSON).content(asJsonString(request)))
        .andDo(print())
        .andExpect(status().isOk()); // then
  }

  @Test
  @DisplayName("Test createPriceUnhappy()")
  void testCreatePriceUnhappy() throws Exception {
    // given
    var malformedJsonString = "thisWontWorkLol";

    // when
    mockMvc
        .perform(post("/api/price").contentType(APPLICATION_JSON).content(malformedJsonString))
        .andDo(print())
        .andExpect(status().is4xxClientError()); // then
  }

  @Test
  @DisplayName("Test getProductByIdHappy")
  void testGetProductByIdHappy() throws Exception {
    // given
    Long sampleId = Long.valueOf("123");
    BigDecimal sampleValue = BigDecimal.valueOf(Long.valueOf("10"));
    Currency sampleCurrency = Currency.getInstance("USD");
    Price samplePrice =
        Price.builder().id(sampleId).value(sampleValue).currencyCode(sampleCurrency).build();
    Product sampleProduct =
        Product.builder().id(sampleId).name("product").price(samplePrice).build();
    var url = "https://fake/api/product/" + sampleId.toString();

    // when
    when(apiService.populateProductObject(sampleId.toString()))
        .thenReturn(Optional.of(sampleProduct));
    mockMvc
        .perform(get(url).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk()); // then
  }

  @Test
  @DisplayName("Test getProductByIdUnhappy")
  void testGetProductByIdUnhappy() throws Exception {
    // given
    var url = "https://fake/api/product/NOT_A_REAL_PRODUCT";

    // when
    when(apiService.populateProductObject(Mockito.anyString())).thenReturn(Optional.empty());
    mockMvc
        .perform(get(url).contentType(APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().is4xxClientError()); // then
  }

  @Test
  @DisplayName("Test updateProductByIdHappy")
  void testUpdateProductByIdHappy() throws Exception {
    // given
    String sampleIdAsString = "123";
    Long sampleId = Long.valueOf(sampleIdAsString);

    String sampleValueAsString = "10";
    BigDecimal sampleValue = BigDecimal.valueOf(Long.valueOf(sampleValueAsString));

    String updatedSampleValueAsString = "1";
    BigDecimal updatedSampleValue = BigDecimal.valueOf(Long.valueOf(updatedSampleValueAsString));

    String sampleCurrencyAsString = "USD";
    Currency sampleCurrency = Currency.getInstance(sampleCurrencyAsString);

    Price samplePrice =
        Price.builder().id(sampleId).value(sampleValue).currencyCode(sampleCurrency).build();
    Price updatedSamplePrice =
        Price.builder().id(sampleId).value(updatedSampleValue).currencyCode(sampleCurrency).build();
    PriceRequestObject request =
        PriceRequestObject.builder()
            .id(sampleIdAsString)
            .value(updatedSampleValueAsString)
            .currencyCode(sampleCurrencyAsString)
            .build();

    Product sampleProduct =
        Product.builder().id(sampleId).name("product").price(samplePrice).build();
    var url = "https://fake/api/product/" + sampleIdAsString;

    // when
    when(apiService.populateProductObject(Mockito.any()))
        .thenReturn(Optional.of(sampleProduct));
    when(priceService.updatePriceById(Mockito.any(), Mockito.any()))
        .thenReturn(Optional.of(updatedSamplePrice));
    mockMvc
        .perform(put(url).contentType(APPLICATION_JSON).content(asJsonString(request)))
        .andDo(print())
        .andExpect(status().isOk()); // then
  }

  @Test
  @DisplayName("Test updateProductByIdUnhappy empty product")
  void testUpdateProductByIdUnhappyEmptyProduct() throws Exception {
    // given
    String sampleIdAsString = "123";
    Long sampleId = Long.valueOf(sampleIdAsString);

    String sampleValueAsString = "10";
    BigDecimal sampleValue = BigDecimal.valueOf(Long.valueOf(sampleValueAsString));

    String updatedSampleValueAsString = "1";
    BigDecimal updatedSampleValue = BigDecimal.valueOf(Long.valueOf(updatedSampleValueAsString));

    String sampleCurrencyAsString = "USD";
    Currency sampleCurrency = Currency.getInstance(sampleCurrencyAsString);

    Price updatedSamplePrice =
            Price.builder().id(sampleId).value(updatedSampleValue).currencyCode(sampleCurrency).build();
    PriceRequestObject request =
            PriceRequestObject.builder()
                    .id(sampleIdAsString)
                    .value(updatedSampleValueAsString)
                    .currencyCode(sampleCurrencyAsString)
                    .build();

    var url = "https://fake/api/product/" + sampleIdAsString;

    // when
    when(apiService.populateProductObject(Mockito.any()))
            .thenReturn(Optional.empty());
    when(priceService.updatePriceById(Mockito.any(), Mockito.any()))
            .thenReturn(Optional.of(updatedSamplePrice));
    mockMvc
            .perform(put(url).contentType(APPLICATION_JSON).content(asJsonString(request)))
            .andDo(print())
            .andExpect(status().is4xxClientError()); // then
  }

  @Test
  @DisplayName("Test updateProductByIdUnhappy empty price")
  void testUpdateProductByIdUnhappyEmptyPrice() throws Exception {
    // given
    String sampleIdAsString = "123";
    Long sampleId = Long.valueOf(sampleIdAsString);

    String sampleValueAsString = "10";
    BigDecimal sampleValue = BigDecimal.valueOf(Long.valueOf(sampleValueAsString));

    String updatedSampleValueAsString = "1";
    BigDecimal updatedSampleValue = BigDecimal.valueOf(Long.valueOf(updatedSampleValueAsString));

    String sampleCurrencyAsString = "USD";
    Currency sampleCurrency = Currency.getInstance(sampleCurrencyAsString);

    Price samplePrice =
            Price.builder().id(sampleId).value(sampleValue).currencyCode(sampleCurrency).build();
    Price updatedSamplePrice =
            Price.builder().id(sampleId).value(updatedSampleValue).currencyCode(sampleCurrency).build();
    PriceRequestObject request =
            PriceRequestObject.builder()
                    .id(sampleIdAsString)
                    .value(updatedSampleValueAsString)
                    .currencyCode(sampleCurrencyAsString)
                    .build();
    Product sampleProduct =
            Product.builder().id(sampleId).name("product").price(samplePrice).build();
    var url = "https://fake/api/product/" + sampleIdAsString;

    // when
    when(apiService.populateProductObject(Mockito.any()))
            .thenReturn(Optional.of(sampleProduct));
    when(priceService.updatePriceById(Mockito.any(), Mockito.any()))
            .thenReturn(Optional.empty());
    mockMvc
            .perform(put(url).contentType(APPLICATION_JSON).content(asJsonString(request)))
            .andDo(print())
            .andExpect(status().is4xxClientError()); // then
  }
  //could also test empty body or empty fields for each but this is meant to be a POC
}
