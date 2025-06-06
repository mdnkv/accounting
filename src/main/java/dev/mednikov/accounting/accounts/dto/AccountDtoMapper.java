package dev.mednikov.accounting.accounts.dto;

import dev.mednikov.accounting.accounts.models.Account;

import java.util.function.Function;

public final class AccountDtoMapper implements Function<Account, AccountDto> {

    @Override
    public AccountDto apply(Account account) {
        AccountDto result = new AccountDto();
        result.setId(account.getId().toString());
        result.setName(account.getName());
        result.setCode(account.getCode());
        result.setAccountType(account.getAccountType());
        result.setOrganizationId(account.getOrganization().getId().toString());
        return result;
    }

}
