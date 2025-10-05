package dev.mednikov.accounting.accounts.dto;

import dev.mednikov.accounting.accounts.models.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public final class AccountDto {

    private String id;
    @NotNull @NotBlank private String organizationId;
    @NotNull @NotBlank @Length(max=255) private String name;
    @NotNull @NotBlank @Length(max=20) private String code;
    @NotNull private AccountType accountType;
    private boolean deprecated;
    private String accountCategoryId;
    private AccountCategoryDto accountCategory;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public String getAccountCategoryId() {
        return accountCategoryId;
    }

    public void setAccountCategoryId(String accountCategoryId) {
        this.accountCategoryId = accountCategoryId;
    }

    public AccountCategoryDto getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(AccountCategoryDto accountCategory) {
        this.accountCategory = accountCategory;
    }
}
