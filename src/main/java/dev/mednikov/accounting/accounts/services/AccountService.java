package dev.mednikov.accounting.accounts.services;

import dev.mednikov.accounting.accounts.dto.AccountDto;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountDto createAccount(AccountDto accountDto);

    AccountDto updateAccount(AccountDto accountDto);

    void deleteAccount(Long id);

    List<AccountDto> getAccounts(Long organizationId);

    Optional<AccountDto> getAccount(Long id);

}
