package com.testapp.currencyservice.converter;

import com.testapp.currencyservice.entity.ExchangeRate;
import com.testapp.currencyservice.model.apiclient.MonobankExchangeRateResponse;
import com.testapp.currencyservice.model.exchange_rate.ExchangeRateResponse;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.time.LocalDateTime;

@Component
public class ExchangeRateConverter {

    public ExchangeRate convertToEntity(MonobankExchangeRateResponse monobankExchangeRateResponse) {
        return ExchangeRate.builder()
                .currencyCodeFrom(monobankExchangeRateResponse.currencyCodeA())
                .currencyCodeTo(monobankExchangeRateResponse.currencyCodeB())
                .buyRate(monobankExchangeRateResponse.rateBuy().setScale(4, RoundingMode.HALF_UP))
                .sellRate(monobankExchangeRateResponse.rateSell().setScale(4, RoundingMode.HALF_UP))
                .dateRate(monobankExchangeRateResponse.date())
                .insertDate(LocalDateTime.now())
                .build();
    }

    public ExchangeRateResponse convertToExchangeRateResponse(ExchangeRate exchangeRate) {
        return new ExchangeRateResponse(exchangeRate.getCurrencyCodeFrom(),
                exchangeRate.getCurrencyCodeTo(), exchangeRate.getBuyRate(), exchangeRate.getSellRate(),
                exchangeRate.getDateRate());
    }
}
