package com.example.demo.repos;

import com.example.demo.models.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class PriceRepositoryTests {

    @Autowired
    private PriceRepository repo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void setup() throws Exception {
        repo.deleteAll();
    }



    @DisplayName("given a new price"
            + " when saving that price"
            + " then price is saved")
    @Test
    public void testSave() {
        // given
        Long sampleId = Long.valueOf("123");
        Price newPrice = Price.builder()
                .id(sampleId)
                        .value(BigDecimal.TEN)
                                .currencyCode(Currency.getInstance("USD"))
                                        .build();

        // when
        repo.save(newPrice);

        // then
        assertEquals(repo.findAll().get(0).getId(), sampleId);
    }

    @DisplayName("given a new price"
            + " when finding that price by id"
            + " then price is same")
    @Test
    public void testFindById() {
        // given
        Long sampleId = Long.valueOf("123");
        Price originalPrice = Price.builder()
                .id(sampleId)
                .value(BigDecimal.TEN)
                .currencyCode(Currency.getInstance("USD"))
                .build();
        repo.save(originalPrice);

        // when
        Optional<Price> actualPrice = repo.findById(sampleId);

        // then
        assertEquals(actualPrice.get(), originalPrice);
    }

    @DisplayName("given a new price"
            + " when finding that price by value"
            + " then price is same")
    @Test
    public void testFindByValue() {
        // given
        BigDecimal sampleValue = BigDecimal.TEN;
        Price originalPrice = Price.builder()
                .id(Long.valueOf("123"))
                .value(sampleValue)
                .currencyCode(Currency.getInstance("USD"))
                .build();
        repo.save(originalPrice);

        // when
        Optional<List<Price>> actualPrice = repo.findByValue(sampleValue);

        // then
        assertEquals(actualPrice.get().get(0), originalPrice);
    }
}
