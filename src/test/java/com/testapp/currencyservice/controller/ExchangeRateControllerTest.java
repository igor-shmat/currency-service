package com.testapp.currencyservice.controller;

import com.testapp.currencyservice.converter.ExchangeRateConverter;
import com.testapp.currencyservice.entity.ExchangeRate;
import com.testapp.currencyservice.repository.ExchangeRateRepository;
import com.testapp.currencyservice.service.ExchangeRateService;
import com.testapp.currencyservice.service.apiclient.ExchangeApi;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest
@Import({ExchangeRateService.class, ExchangeRateConverter.class})
class ExchangeRateControllerTest {

    @MockBean
    private ExchangeRateRepository exchangeRateRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ExchangeRateConverter exchangeRateConverter;

    @MockBean
    private ExchangeApi exchangeApi;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getAll_works_success() {
        //Given
        BDDMockito.given(exchangeRateService.getAllExchangeRates())
                .willReturn(Flux.just(ExchangeRate.builder()
                                .id(1L)
                                .currencyCodeFrom((short) 840)
                                .currencyCodeTo((short) 980)
                                .buyRate(BigDecimal.valueOf(37.0000))
                                .sellRate(BigDecimal.valueOf(38.0000))
                                .dateRate(LocalDateTime.parse("2023-11-29T12:00:00.000"))
                                .insertDate(LocalDateTime.parse("2023-11-29T13:00:00.000"))
                                .build(),
                        ExchangeRate.builder()
                                .id(1L)
                                .currencyCodeFrom((short) 978)
                                .currencyCodeTo((short) 980)
                                .buyRate(BigDecimal.valueOf(40.0000))
                                .sellRate(BigDecimal.valueOf(41.0000))
                                .dateRate(LocalDateTime.parse("2023-11-29T12:00:00.000"))
                                .insertDate(LocalDateTime.parse("2023-11-29T13:00:00.000"))
                                .build(),
                        ExchangeRate.builder()
                                .id(1L)
                                .currencyCodeFrom((short) 840)
                                .currencyCodeTo((short) 980)
                                .buyRate(BigDecimal.valueOf(1.1000))
                                .sellRate(BigDecimal.valueOf(1.2000))
                                .dateRate(LocalDateTime.parse("2023-11-29T12:00:00.000"))
                                .insertDate(LocalDateTime.parse("2023-11-29T13:00:00.000"))
                                .build(),
                        ExchangeRate.builder()
                                .id(1L)
                                .currencyCodeFrom((short) 840)
                                .currencyCodeTo((short) 980)
                                .buyRate(BigDecimal.valueOf(38.0000))
                                .sellRate(BigDecimal.valueOf(39.0000))
                                .dateRate(LocalDateTime.parse("2023-11-29T13:00:00.000"))
                                .insertDate(LocalDateTime.parse("2023-11-29T14:00:00.000"))
                                .build()
                ));
        //Then
        webTestClient
                .get()
                .uri("/exchangeRate/getAll")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("[{\"currencyCodeFrom\":840,\"currencyCodeTo\":980,\"buyRate\":37.0,\"sellRate\":38.0,\"dateRate\":\"2023-11-29T12:00:00\"},{\"currencyCodeFrom\":978,\"currencyCodeTo\":980,\"buyRate\":40.0,\"sellRate\":41.0,\"dateRate\":\"2023-11-29T12:00:00\"},{\"currencyCodeFrom\":840,\"currencyCodeTo\":980,\"buyRate\":1.1,\"sellRate\":1.2,\"dateRate\":\"2023-11-29T12:00:00\"},{\"currencyCodeFrom\":840,\"currencyCodeTo\":980,\"buyRate\":38.0,\"sellRate\":39.0,\"dateRate\":\"2023-11-29T13:00:00\"}]");
    }

    @Test
    public void getAllToday_works_success() {
        //Given
        BDDMockito.given(exchangeRateService.getAllTodayExchangeRates())
                .willReturn(Flux.just(ExchangeRate.builder()
                                .id(1L)
                                .currencyCodeFrom((short) 840)
                                .currencyCodeTo((short) 980)
                                .buyRate(BigDecimal.valueOf(37.0000))
                                .sellRate(BigDecimal.valueOf(38.0000))
                                .dateRate(LocalDateTime.parse("2023-11-29T12:00:00.000"))
                                .insertDate(LocalDateTime.parse("2023-11-29T13:00:00.000"))
                                .build(),
                        ExchangeRate.builder()
                                .id(1L)
                                .currencyCodeFrom((short) 978)
                                .currencyCodeTo((short) 980)
                                .buyRate(BigDecimal.valueOf(40.0000))
                                .sellRate(BigDecimal.valueOf(41.0000))
                                .dateRate(LocalDateTime.parse("2023-11-29T12:00:00.000"))
                                .insertDate(LocalDateTime.parse("2023-11-29T13:00:00.000"))
                                .build(),
                        ExchangeRate.builder()
                                .id(1L)
                                .currencyCodeFrom((short) 840)
                                .currencyCodeTo((short) 980)
                                .buyRate(BigDecimal.valueOf(1.1000))
                                .sellRate(BigDecimal.valueOf(1.2000))
                                .dateRate(LocalDateTime.parse("2023-11-29T12:00:00.000"))
                                .insertDate(LocalDateTime.parse("2023-11-29T13:00:00.000"))
                                .build()
                ));
        //Then
        webTestClient
                .get()
                .uri("/exchangeRate/getAllToday")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .json("[{\"currencyCodeFrom\":840,\"currencyCodeTo\":980,\"buyRate\":37.0,\"sellRate\":38.0,\"dateRate\":\"2023-11-29T12:00:00\"},{\"currencyCodeFrom\":978,\"currencyCodeTo\":980,\"buyRate\":40.0,\"sellRate\":41.0,\"dateRate\":\"2023-11-29T12:00:00\"},{\"currencyCodeFrom\":840,\"currencyCodeTo\":980,\"buyRate\":1.1,\"sellRate\":1.2,\"dateRate\":\"2023-11-29T12:00:00\"}]");
    }
}