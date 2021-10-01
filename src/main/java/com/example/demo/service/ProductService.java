package com.example.demo.service;

import com.example.demo.utils.JsonParseException;
import com.example.demo.utils.JsonUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final JsonUtils jsonUtils;

  private static final String BASE_URL = "https://redsky.target.com";

  private final WebClient client = WebClient.create(BASE_URL);

  public Mono<String> getProductDataResponse(String productId) {
    String productEndpoint =
    format("/v3/pdp/tcin/%s?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics&key=candidate#_blank", productId);
    return client
        .get()
        .uri(productEndpoint)
        .accept(MediaType.APPLICATION_JSON)
        .exchangeToMono(
            response -> {
              if (response.statusCode().equals(HttpStatus.OK)) {
                return response.bodyToMono(String.class);
              } else if (response.statusCode().is4xxClientError()) {
                return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product Request Failed, 4xx error"));
              } else {
                return response.createException().flatMap(Mono::error);
              }
            });
  }

  public Optional<String> getProductNameFromResponse(String productId) {
    try {
      var responseString = getProductDataResponse(productId).block();
      var productDataJson = jsonUtils.parseStringToJsonObject(responseString);
      return Optional.of(jsonUtils.getNameFromProductJson(productDataJson));
    } catch (JsonParseException e) {
      return Optional.empty();
    }
  }
}
