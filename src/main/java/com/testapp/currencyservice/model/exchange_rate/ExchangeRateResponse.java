package com.testapp.currencyservice.model.exchange_rate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExchangeRateResponse(

        Short currencyCodeFrom,

        Short currencyCodeTo,

        BigDecimal buyRate,

        BigDecimal sellRate,

        LocalDateTime dateRate) {
}
