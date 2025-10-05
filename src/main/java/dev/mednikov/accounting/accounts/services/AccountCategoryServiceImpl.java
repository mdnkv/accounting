package dev.mednikov.accounting.accounts.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.accounts.dto.AccountCategoryDto;
import dev.mednikov.accounting.accounts.dto.AccountCategoryDtoMapper;
import dev.mednikov.accounting.accounts.exceptions.AccountCategoryAlreadyExistsException;
import dev.mednikov.accounting.accounts.exceptions.AccountCategoryNotFoundException;
import dev.mednikov.accounting.accounts.models.AccountCategory;
import dev.mednikov.accounting.accounts.repositories.AccountCategoryRepository;
import dev.mednikov.accounting.organizations.exceptions.OrganizationNotFoundException;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AccountCategoryServiceImpl implements AccountCategoryService {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
    private final AccountCategoryDtoMapper mapper = new AccountCategoryDtoMapper();

    private final OrganizationRepository organizationRepository;
    private final AccountCategoryRepository accountCategoryRepository;

    public AccountCategoryServiceImpl(OrganizationRepository organizationRepository, AccountCategoryRepository accountCategoryRepository) {
        this.organizationRepository = organizationRepository;
        this.accountCategoryRepository = accountCategoryRepository;
    }

    @Override
    public AccountCategoryDto createAccountCategory(AccountCategoryDto accountCategoryDto) {
        Long organizationId = Long.parseLong(accountCategoryDto.getOrganizationId());
        Organization organization = this.organizationRepository.findById(organizationId)
                .orElseThrow(OrganizationNotFoundException::new);

        String name = accountCategoryDto.getName();
        if (this.accountCategoryRepository.findByOrganizationIdAndName(organizationId, name).isPresent()) {
            throw new AccountCategoryAlreadyExistsException();
        }

        AccountCategory accountCategory = new AccountCategory();
        accountCategory.setOrganization(organization);
        accountCategory.setName(name);
        accountCategory.setAccountType(accountCategoryDto.getAccountType());
        accountCategory.setId(snowflakeGenerator.next());

        AccountCategory result = this.accountCategoryRepository.save(accountCategory);
        return mapper.apply(result);
    }

    @Override
    public AccountCategoryDto updateAccountCategory(AccountCategoryDto accountCategoryDto) {
        Objects.requireNonNull(accountCategoryDto.getId());
        Long id = Long.parseLong(accountCategoryDto.getId());
        AccountCategory accountCategory = this.accountCategoryRepository.findById(id)
                .orElseThrow(AccountCategoryNotFoundException::new);

        // verify that the name is not occupied
        if (!accountCategory.getName().equals(accountCategoryDto.getName())) {
            String name = accountCategoryDto.getName();
            Long organizationId = Long.parseLong(accountCategoryDto.getOrganizationId());
            if (this.accountCategoryRepository.findByOrganizationIdAndName(organizationId, name).isPresent()) {
                throw new AccountCategoryAlreadyExistsException();
            }
        }
        accountCategory.setName(accountCategoryDto.getName());
        accountCategory.setAccountType(accountCategoryDto.getAccountType());
        AccountCategory result = this.accountCategoryRepository.save(accountCategory);
        return mapper.apply(result);
    }

    @Override
    public void deleteAccountCategory(Long id) {
        this.accountCategoryRepository.deleteById(id);

    }

    @Override
    public List<AccountCategoryDto> getAccountCategories(Long organizationId) {
        return this.accountCategoryRepository.findByOrganizationId(organizationId)
                .stream()
                .map(mapper)
                .toList();
    }
}
