package com.testapp.currencyservice.model.apiclient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MonobankExchangeRateResponse(
        Short currencyCodeA,
        Short currencyCodeB,
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime date,
        BigDecimal rateBuy,
        BigDecimal rateSell) {


}
