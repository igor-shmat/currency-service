package com.testapp.currencyservice.service.apiclient;

import com.testapp.currencyservice.model.apiclient.MonobankExchangeRateResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Objects;

@Log4j2
@Service
public class ExchangeApi {

    @Value("${apiclient.mono.url}")
    private String uri;
    private final WebClient webClient;

    @Autowired
    public ExchangeApi(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<MonobankExchangeRateResponse> getExchangeRates() {
        return webClient.get()
                .uri(uri + "/bank/currency")
                .retrieve()
                .bodyToFlux(MonobankExchangeRateResponse.class)
                .filter(monobankExchangeRateResponse -> Objects.nonNull(monobankExchangeRateResponse.rateSell()) &&
                                                        Objects.nonNull(monobankExchangeRateResponse.rateBuy()))
                .doOnNext(log::info)
                .doOnError(error -> log.error("There is an error while sending request {}", error.getMessage()));
    }
}
