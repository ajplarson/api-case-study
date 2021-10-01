package com.example.demo.service;

import com.example.demo.utils.InvalidQueryException;
import com.example.demo.utils.JsonParseException;
import com.example.demo.utils.JsonUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final JsonUtils jsonUtils;

  private static final String BASE_URL = "https://redsky.target.com";
  private static final String DATA_ENDPOINT =
      "/v3/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics&key=candidate#_blank";
  private final WebClient client = WebClient.create(BASE_URL);

  public Mono<String> getProductDataResponse() {
    return client
        .get()
        .uri(DATA_ENDPOINT)
        .accept(MediaType.APPLICATION_JSON)
        .exchangeToMono(
            response -> {
              if (response.statusCode().equals(HttpStatus.OK)) {
                return response.bodyToMono(String.class);
              } else if (response.statusCode().is4xxClientError()) {
                return Mono.error(new InvalidQueryException("Product Request Failed"));
              } else {
                return response.createException().flatMap(Mono::error);
              }
            });
  }

  //  because the sample json does not have an array of products but just a single json object,
  //  the below wont compile but this is how i would do it
  //  public Optional<JsonObject> getProductById(String productId) throws JsonParseException {
  //      JsonArray responseJsonArray = new JsonArray();
  //      JsonObject currentProductObject;
  //      for (JsonElement currentElement : responseJsonArray) {
  //          currentProductObject = currentElement.getAsJsonObject();
  //          if(productId.equals(getIdFromProductJson(currentProductObject))) {
  //                return Optional.of(currentProductObject);
  //          }
  //      }
  //      return Optional.empty();
  //  }

  public Optional<String> getProductIdFromResponse() {
    try {
      var responseString = getProductDataResponse().block();
      var productDataJson = jsonUtils.parseStringToJsonObject(responseString);
      return Optional.of(jsonUtils.getIdFromProductJson(productDataJson));
    } catch (JsonParseException e) {
      return Optional.empty();
    }
  }

  public Optional<String> getProductNameFromResponse() {
    try {
      var responseString = getProductDataResponse().block();
      var productDataJson = jsonUtils.parseStringToJsonObject(responseString);
      return Optional.of(jsonUtils.getNameFromProductJson(productDataJson));
    } catch (JsonParseException e) {
      return Optional.empty();
    }
  }
}
