package dev.mednikov.accounting.currencies.clients;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

final class CurrencyExchangeRateResponse {

    String base;
    LocalDate date;
    Map<String, BigDecimal> rates;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
