package dev.mednikov.accounting.accounts.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.accounts.dto.AccountDto;
import dev.mednikov.accounting.accounts.dto.AccountDtoMapper;
import dev.mednikov.accounting.accounts.exceptions.AccountAlreadyExistsException;
import dev.mednikov.accounting.accounts.exceptions.AccountDeletionException;
import dev.mednikov.accounting.accounts.exceptions.AccountNotFoundException;
import dev.mednikov.accounting.accounts.models.Account;
import dev.mednikov.accounting.accounts.models.AccountCategory;
import dev.mednikov.accounting.accounts.repositories.AccountCategoryRepository;
import dev.mednikov.accounting.accounts.repositories.AccountRepository;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import dev.mednikov.accounting.transactions.repositories.TransactionLineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final static AccountDtoMapper accountDtoMapper = new AccountDtoMapper();
    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    private final AccountRepository accountRepository;
    private final AccountCategoryRepository accountCategoryRepository;
    private final TransactionLineRepository transactionLineRepository;
    private final OrganizationRepository organizationRepository;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            OrganizationRepository organizationRepository,
            TransactionLineRepository transactionLineRepository,
            AccountCategoryRepository accountCategoryRepository) {
        this.accountRepository = accountRepository;
        this.organizationRepository = organizationRepository;
        this.transactionLineRepository = transactionLineRepository;
        this.accountCategoryRepository = accountCategoryRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Long organizationId = Long.valueOf(accountDto.getOrganizationId());
        String code = accountDto.getCode();
        if (this.accountRepository.findByOrganizationIdAndCode(organizationId,code).isPresent()) {
            throw new AccountAlreadyExistsException();
        }

        Organization organization = this.organizationRepository.getReferenceById(organizationId);

        Account account = new Account();
        account.setCode(code);
        account.setName(accountDto.getName());
        account.setId(snowflakeGenerator.next());
        account.setAccountType(accountDto.getAccountType());
        account.setOrganization(organization);
        account.setDeprecated(false);

        // Set category
        if (accountDto.getAccountCategoryId() != null) {
            Long accountCategoryId = Long.valueOf(accountDto.getAccountCategoryId());
            AccountCategory accountCategory = this.accountCategoryRepository.getReferenceById(accountCategoryId);
            account.setAccountCategory(accountCategory);
        } else {
            account.setAccountCategory(null);
        }

        Account result = this.accountRepository.save(account);

        return accountDtoMapper.apply(result);
    }

    @Override
    public AccountDto updateAccount(AccountDto accountDto) {
        Objects.requireNonNull(accountDto.getId());
        Long accountId = Long.valueOf(accountDto.getId());
        Account account = this.accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);

        account.setName(accountDto.getName());
        account.setAccountType(accountDto.getAccountType());
        account.setDeprecated(accountDto.isDeprecated());

        if (!account.getCode().equals(accountDto.getCode())) {
            Long organizationId = Long.valueOf(accountDto.getOrganizationId());
            String code = accountDto.getCode();
            if (this.accountRepository.findByOrganizationIdAndCode(organizationId, code).isPresent()) {
                throw new AccountAlreadyExistsException();
            }
            account.setCode(code);
        }

        if (accountDto.getAccountCategoryId() != null) {
            Long accountCategoryId = Long.valueOf(accountDto.getAccountCategoryId());
            AccountCategory accountCategory = this.accountCategoryRepository.getReferenceById(accountCategoryId);
            account.setAccountCategory(accountCategory);
        } else {
            account.setAccountCategory(null);
        }

        Account result = this.accountRepository.save(account);
        return accountDtoMapper.apply(result);
    }

    @Override
    public void deleteAccount(Long id) {
        if (this.transactionLineRepository.findByAccountId(id).isEmpty()){
            this.accountRepository.deleteById(id);
        } else {
            throw new AccountDeletionException();
        }
    }

    @Override
    public List<AccountDto> getAccounts(Long organizationId, boolean includeDeprecated) {
        if (includeDeprecated) {
            return this.accountRepository.findAllByOrganizationId(organizationId)
                    .stream()
                    .map(accountDtoMapper)
                    .toList();
        } else {
            return this.accountRepository.findActiveByOrganizationId(organizationId)
                    .stream()
                    .map(accountDtoMapper)
                    .toList();
        }
    }

    @Override
    public Optional<AccountDto> getAccount(Long id) {
        return this.accountRepository.findById(id).map(accountDtoMapper);
    }
}
