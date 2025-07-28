package dev.mednikov.accounting.currencies.clients;

import dev.mednikov.accounting.currencies.models.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CurrencyExchangeRestClient {

    BigDecimal getExchangeRate (LocalDate date, Currency baseCurrency, Currency targetCurrency);

}
