package dev.mednikov.accounting.currencies.clients;

import dev.mednikov.accounting.currencies.models.Currency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class CurrencyExchangeRestClientImpl implements CurrencyExchangeRestClient {

    private final RestClient restClient;

    public CurrencyExchangeRestClientImpl() {
        this.restClient = RestClient.builder().baseUrl("https://api.frankfurter.dev/v1/").build();
    }

    @Override
    public BigDecimal getExchangeRate(LocalDate d, Currency bc, Currency tc) {
        String baseCurrency = bc.getCode();
        String targetCurrency = tc.getCode();
        String date = d.toString();

        String endpoint = "{date}?base={baseCurrency}&symbols={targetCurrency}";
        CurrencyExchangeRateResponse response = this.restClient.get()
                .uri(endpoint, date, baseCurrency, targetCurrency)
                .retrieve()
                .body(CurrencyExchangeRateResponse.class);
        return response.getRates().get(targetCurrency);
    }
}
