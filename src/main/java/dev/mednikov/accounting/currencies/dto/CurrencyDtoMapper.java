package dev.mednikov.accounting.currencies.dto;

import dev.mednikov.accounting.currencies.models.Currency;

import java.util.function.Function;

public final class CurrencyDtoMapper implements Function<Currency, CurrencyDto> {

    @Override
    public CurrencyDto apply(Currency currency) {
        CurrencyDto result = new CurrencyDto();
        result.setName(currency.getName());
        result.setCode(currency.getCode());
        result.setPrimary(currency.isPrimary());
        result.setId(currency.getId().toString());
        result.setOrganizationId(currency.getOrganization().getId().toString());
        return result;
    }
}
