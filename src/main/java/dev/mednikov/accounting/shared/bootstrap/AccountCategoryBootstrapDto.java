package dev.mednikov.accounting.shared.bootstrap;

import dev.mednikov.accounting.accounts.models.AccountType;

final class AccountCategoryBootstrapDto {

    private String name;
    private AccountType accountType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
