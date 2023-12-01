package com.testapp.currencyservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(schema = "currency_data", name = "exchange_rates")
public class ExchangeRate {

    @Id
    @EqualsAndHashCode.Exclude
    private Long id;

    private Short currencyCodeFrom;

    private Short currencyCodeTo;

    private BigDecimal buyRate;

    private BigDecimal sellRate;

    private LocalDateTime dateRate;

    @EqualsAndHashCode.Exclude
    private LocalDateTime insertDate;

}
