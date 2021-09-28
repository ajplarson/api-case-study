package com.example.demo.service;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final String BASE_URL = "https://redsky.target.com";
    private static final String DATA_ENDPOINT = "/v3/pdp/tcin/13860428?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics&key=candidate#_blank";
    private static final WebClient client = WebClient.create(BASE_URL);
//    private Mono<String> getAllData() {
//
//    }

    public static void main(String[] args) {
        WebClient.ResponseSpec responseSpec = client.get().uri(DATA_ENDPOINT).retrieve();
        var responseBody = responseSpec.bodyToMono(String.class).block();
        System.out.println(responseBody);
    }
}
