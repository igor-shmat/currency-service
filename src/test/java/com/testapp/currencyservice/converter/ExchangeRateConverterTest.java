package com.testapp.currencyservice.converter;

import com.testapp.currencyservice.entity.ExchangeRate;
import com.testapp.currencyservice.model.apiclient.MonobankExchangeRateResponse;
import com.testapp.currencyservice.model.exchange_rate.ExchangeRateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExchangeRateConverterTest {

    private ExchangeRateConverter converter;

    @BeforeEach
    void setUp() {
        converter = new ExchangeRateConverter();
    }

    @Test
    void convertToEntity_shouldConvertMonobankResponseToEntity() {
        //Given
        MonobankExchangeRateResponse response = mock(MonobankExchangeRateResponse.class);
        when(response.currencyCodeA()).thenReturn((short) 840);
        when(response.currencyCodeB()).thenReturn((short) 978);
        when(response.rateBuy()).thenReturn(BigDecimal.valueOf(1.22));
        when(response.rateSell()).thenReturn(BigDecimal.valueOf(1.23));
        when(response.date()).thenReturn(LocalDateTime.now());

        //When
        ExchangeRate result = converter.convertToEntity(response);

        //Then
        assertEquals((short) 840, result.getCurrencyCodeFrom());
        assertEquals((short) 978, result.getCurrencyCodeTo());
        assertEquals(BigDecimal.valueOf(1.22).setScale(4, RoundingMode.HALF_UP), result.getBuyRate());
        assertEquals(BigDecimal.valueOf(1.23).setScale(4, RoundingMode.HALF_UP), result.getSellRate());
        assertEquals(response.date(), result.getDateRate());
    }

    @Test
    void convertToExchangeRateResponse_shouldConvertEntityToResponse() {
        //Given
        ExchangeRate entity = new ExchangeRate(1L, (short) 840, (short) 978, BigDecimal.valueOf(1.22), BigDecimal.valueOf(1.23), LocalDateTime.now(), LocalDateTime.now());

        //When
        ExchangeRateResponse result = converter.convertToExchangeRateResponse(entity);

        //Then
        assertEquals((short) 840, result.currencyCodeFrom());
        assertEquals((short) 978, result.currencyCodeTo());
        assertEquals(BigDecimal.valueOf(1.22), result.buyRate());
        assertEquals(BigDecimal.valueOf(1.23), result.sellRate());
        assertEquals(entity.getDateRate(), result.dateRate());
    }
}