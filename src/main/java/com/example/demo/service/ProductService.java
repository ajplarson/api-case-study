package com.example.demo.service;

import com.example.demo.utils.JsonUtils;
import java.util.Optional;

import com.google.gson.JsonParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final JsonUtils jsonUtils;

  private static final String BASE_URL = "https://redsky.target.com";

  private final WebClient client = WebClient.create(BASE_URL);
  
  private String getProductResponseAsString(String productId) {
    String productEndpoint =
            format("/v3/pdp/tcin/%s?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics&key=candidate#_blank", productId);
    var responseSpec = client
            .get()
            .uri(productEndpoint)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve();
    return responseSpec.
            bodyToMono(String.class).block();
  }

  public Optional<String> getProductNameFromResponse(String productId) {
    try {
      var responseString = getProductResponseAsString(productId);
      var productDataJson = jsonUtils.parseStringToJsonObject(responseString);
      return Optional.of(jsonUtils.getNameFromProductJson(productDataJson));
    } catch (JsonParseException jsonParseException) {
      return Optional.empty();
    }
  }
}
