package com.testapp.currencyservice.service;

import com.testapp.currencyservice.converter.ExchangeRateConverter;
import com.testapp.currencyservice.entity.ExchangeRate;
import com.testapp.currencyservice.exception.EntityNotFoundException;
import com.testapp.currencyservice.repository.ExchangeRateRepository;
import com.testapp.currencyservice.service.apiclient.ExchangeApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeApi exchangeApi;

    private final ExchangeRateRepository exchangeRateRepository;

    private final ExchangeRateConverter exchangeRateConverter;

    public Flux<ExchangeRate> updateExchangeRates() {
        Flux<ExchangeRate> exchangeRateFluxApi = exchangeApi.getExchangeRates()
                .map(exchangeRateConverter::convertToEntity);
        Flux<ExchangeRate> exchangeRateFluxDb = this.getAllTodayExchangeRates();

        return Flux.zip(exchangeRateFluxApi.collectList(), exchangeRateFluxDb.collectList())
                .flatMap(tuples -> processExchangeRates(tuples.getT1(), tuples.getT2()));
    }

    private Flux<ExchangeRate> processExchangeRates(List<ExchangeRate> exchangeRatesApi, List<ExchangeRate> exchangeRatesDb) {
        return Flux.fromStream(exchangeRatesApi.stream()
                        .map(apiRate -> updateOrAddExchangeRate(apiRate, exchangeRatesDb))
                        .filter(Objects::nonNull))
                .flatMap(exchangeRateRepository::save);
    }

    private @Nullable ExchangeRate updateOrAddExchangeRate(ExchangeRate exchangeRateApi, List<ExchangeRate> exchangeRatesDb) {
        ExchangeRate existingRate = findRateInList(exchangeRateApi, exchangeRatesDb);

        if (existingRate == null) {
            log.info("___First exchange rates were added___");
            return setInsertDate(exchangeRateApi);
        }

        if (!existingRate.equals(exchangeRateApi)) {
            log.info("___Exchange rate was changed___");
            return createUpdatedExchangeRate(exchangeRateApi, existingRate);
        }

        log.info("___Exchange rate was not changed___");
        return null;
    }

    private ExchangeRate setInsertDate(ExchangeRate exchangeRate) {
        exchangeRate.setInsertDate(LocalDateTime.now());
        return exchangeRate;
    }

    private ExchangeRate createUpdatedExchangeRate(ExchangeRate apiRate, ExchangeRate dbRate) {
        return ExchangeRate.builder()
                .currencyCodeFrom(dbRate.getCurrencyCodeFrom())
                .currencyCodeTo(dbRate.getCurrencyCodeTo())
                .buyRate(apiRate.getBuyRate())
                .sellRate(apiRate.getSellRate())
                .dateRate(apiRate.getDateRate())
                .insertDate(LocalDateTime.now())
                .build();
    }

    private ExchangeRate findRateInList(ExchangeRate targetRate, List<ExchangeRate> rateList) {
        return rateList.stream()
                .filter(rate -> rate.getCurrencyCodeFrom().equals(targetRate.getCurrencyCodeFrom())
                                && rate.getCurrencyCodeTo().equals(targetRate.getCurrencyCodeTo()))
                .findFirst()
                .orElse(null);
    }

    public Flux<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    public Flux<ExchangeRate> getAllTodayExchangeRates() {
        return exchangeRateRepository.findByDateRate(LocalDate.now().atStartOfDay());
    }

    public Mono<ExchangeRate> getByCurrencyCode(Short codeFrom, Short codeTo) {
        return exchangeRateRepository.findByCurrencyCodeFromAndCurrencyCodeTo(codeFrom, codeTo, LocalDate.now().atStartOfDay())
                .switchIfEmpty(Mono.error(new EntityNotFoundException(String
                        .format("Exchange rate not found. CurrencyCodeFrom: %s CurrencyCodeTo: %s", codeFrom, codeTo))));
    }
}
