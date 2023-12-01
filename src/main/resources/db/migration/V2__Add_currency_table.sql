create table if not exists currency_data.exchange_rates(
    id                      bigint GENERATED ALWAYS AS IDENTITY,
    currency_code_from      smallint NOT NULL,
    currency_code_to        smallint NOT NULL,
    buy_rate                numeric(18,4) not null,
    sell_rate               numeric(18,4) not null,
    date_rate               timestamp not null,
    insert_date             timestamp not null,
    constraint exchange_rates_pk primary key (id)
);

CREATE UNIQUE INDEX unique_exchange_rates_idx
ON currency_data.exchange_rates (currency_code_from, currency_code_to, buy_rate, sell_rate, date_rate);