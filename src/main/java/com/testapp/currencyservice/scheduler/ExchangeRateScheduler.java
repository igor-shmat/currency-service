package com.testapp.currencyservice.scheduler;

import com.testapp.currencyservice.entity.ExchangeRate;
import com.testapp.currencyservice.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ExchangeRateScheduler {

    private final ExchangeRateService exchangeRateService;

    @Scheduled(cron = "${scheduler.exchange-rate.cron}")
    public Flux<ExchangeRate> updateExchangeRates() {
        return exchangeRateService.updateExchangeRates();
    }

}
