package dev.mednikov.accounting.accounts.services;

import dev.mednikov.accounting.accounts.dto.AccountCategoryDto;

import java.util.List;

public interface AccountCategoryService {

    AccountCategoryDto createAccountCategory(AccountCategoryDto accountCategoryDto);

    AccountCategoryDto updateAccountCategory(AccountCategoryDto accountCategoryDto);

    void deleteAccountCategory(Long id);

    List<AccountCategoryDto> getAccountCategories(Long organizationId);

}
