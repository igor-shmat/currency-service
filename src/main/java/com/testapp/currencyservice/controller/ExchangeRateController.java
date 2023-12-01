package com.testapp.currencyservice.controller;

import com.testapp.currencyservice.converter.ExchangeRateConverter;
import com.testapp.currencyservice.model.exchange_rate.ExchangeRateResponse;
import com.testapp.currencyservice.service.ExchangeRateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/exchangeRate")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    private final ExchangeRateConverter exchangeRateConverter;

    public ExchangeRateController(ExchangeRateService exchangeRateService, ExchangeRateConverter exchangeRateConverter) {
        this.exchangeRateService = exchangeRateService;
        this.exchangeRateConverter = exchangeRateConverter;
    }

    @GetMapping("/getByCurrencyCode")
    public Mono<ExchangeRateResponse> getByCurrencyCode(@RequestParam Short currencyCodeFrom,
                                                        @RequestParam Short currencyCodeTo
    ) {
        return exchangeRateService.getByCurrencyCode(currencyCodeFrom, currencyCodeTo)
                .map(exchangeRateConverter::convertToExchangeRateResponse);
    }

    @GetMapping("/getFromUSDToUAH")
    public Mono<ExchangeRateResponse> getFromUSDToUAH() {
        return exchangeRateService.getByCurrencyCode((short) 840, (short) 980)
                .map(exchangeRateConverter::convertToExchangeRateResponse);
    }

    @GetMapping("/getAllToday")
    public Flux<ExchangeRateResponse> getAllToday() {
        return exchangeRateService.getAllTodayExchangeRates()
                .map(exchangeRateConverter::convertToExchangeRateResponse);
    }

    @GetMapping("/getAll")
    public Flux<ExchangeRateResponse> getAll() {
        return exchangeRateService.getAllExchangeRates()
                .map(exchangeRateConverter::convertToExchangeRateResponse);
    }
}
