package dev.mednikov.accounting.currencies.services;

import dev.mednikov.accounting.currencies.dto.CurrencyDto;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {

    CurrencyDto createCurrency(CurrencyDto currencyDto);

    CurrencyDto updateCurrency(CurrencyDto currencyDto);

    Optional<CurrencyDto> getPrimaryCurrency (Long organizationId);

    List<CurrencyDto> getCurrencies(Long organizationId);

    void deleteCurrency (Long id);

}
