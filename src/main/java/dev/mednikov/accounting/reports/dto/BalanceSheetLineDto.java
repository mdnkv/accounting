package dev.mednikov.accounting.reports.dto;

import dev.mednikov.accounting.accounts.dto.AccountDto;

import java.math.BigDecimal;

public final class BalanceSheetLineDto {

    private AccountDto account;
    private BigDecimal balance;

    public AccountDto getAccount() {
        return account;
    }

    public void setAccount(AccountDto account) {
        this.account = account;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
