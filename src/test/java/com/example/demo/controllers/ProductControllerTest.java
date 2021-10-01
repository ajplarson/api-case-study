package com.example.demo.controllers;

import com.example.demo.models.Price;
import com.example.demo.models.PriceRequestObject;
import com.example.demo.service.ApiService;
import com.example.demo.service.PriceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Currency;

import static com.example.demo.utils.JsonUtils.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApiService apiService;

    @MockBean private PriceService priceService;

    @Test
    @DisplayName("Test createPrice()")
    void testCreatePrice() throws Exception {
        // given
        String requestId = "123";
        Long sampleId = Long.valueOf(requestId);

        String requestValue = "10";
        BigDecimal sampleValue = BigDecimal.valueOf(Long.valueOf(requestValue));

        String requestCurrency = "USD";
        Currency sampleCurrency = Currency.getInstance(requestCurrency);
        Price samplePrice =
                Price.builder().id(sampleId).value(sampleValue).currencyCode(sampleCurrency).build();

        PriceRequestObject request = PriceRequestObject.builder().id(requestId).value(requestValue).currencyCode(requestCurrency).build();

        //when
        mockMvc.perform(post("/api/price").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isOk()); //then
    }

    @Test
    @DisplayName("Test createPriceUnhappy()")
    void testCreatePriceUnhappy() throws Exception {
        // given
        var malformedJsonString = "thisWontWorkLol";

        //when
        mockMvc.perform(post("/api/price").contentType(MediaType.APPLICATION_JSON).content(malformedJsonString))
                .andDo(print())
                .andExpect(status().is4xxClientError()); //then
    }


}
