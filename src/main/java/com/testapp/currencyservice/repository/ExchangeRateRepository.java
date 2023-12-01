package com.testapp.currencyservice.repository;

import com.testapp.currencyservice.entity.ExchangeRate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRate, Long> {

    @Query("""
            SELECT er.*
            FROM currency_data.exchange_rates AS er
            JOIN (
              SELECT\s
                currency_code_from,\s
                currency_code_to,\s
                MAX(insert_date) AS max_insert_date
              FROM currency_data.exchange_rates
              WHERE date_rate >= :dateTime
              GROUP BY currency_code_from, currency_code_to
            ) AS max_dates\s
            ON er.currency_code_from = max_dates.currency_code_from
               AND er.currency_code_to = max_dates.currency_code_to
               AND er.insert_date = max_dates.max_insert_date
            WHERE er.date_rate >= :dateTime;
            """)
    Flux<ExchangeRate> findByDateRate(LocalDateTime dateTime);

    @Query("""
            SELECT *
            FROM currency_data.exchange_rates AS er
            WHERE er.insert_date = (
                SELECT MAX(insert_date)
                FROM currency_data.exchange_rates
                WHERE currency_code_from = :from
                  AND currency_code_to = :to
                  AND insert_date >= :dateTime
            )
              AND er.currency_code_from = :from
              AND er.currency_code_to = :to
              AND er.insert_date >= :dateTime;
            """)
    Mono<ExchangeRate> findByCurrencyCodeFromAndCurrencyCodeTo(Short from, Short to, LocalDateTime dateTime);
}
