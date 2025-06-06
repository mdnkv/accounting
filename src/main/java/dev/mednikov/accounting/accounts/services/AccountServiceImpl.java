package dev.mednikov.accounting.accounts.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.accounts.dto.AccountDto;
import dev.mednikov.accounting.accounts.dto.AccountDtoMapper;
import dev.mednikov.accounting.accounts.exceptions.AccountAlreadyExistsException;
import dev.mednikov.accounting.accounts.exceptions.AccountNotFoundException;
import dev.mednikov.accounting.accounts.models.Account;
import dev.mednikov.accounting.accounts.repositories.AccountRepository;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {

    private final static AccountDtoMapper accountDtoMapper = new AccountDtoMapper();
    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    private final AccountRepository accountRepository;
    private final OrganizationRepository organizationRepository;

    public AccountServiceImpl(AccountRepository accountRepository, OrganizationRepository organizationRepository) {
        this.accountRepository = accountRepository;
        this.organizationRepository = organizationRepository;
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

        if (!account.getCode().equals(accountDto.getCode())) {
            Long organizationId = Long.valueOf(accountDto.getOrganizationId());
            String code = accountDto.getCode();
            if (this.accountRepository.findByOrganizationIdAndCode(organizationId, code).isPresent()) {
                throw new AccountAlreadyExistsException();
            }
            account.setCode(code);
        }

        Account result = this.accountRepository.save(account);
        return accountDtoMapper.apply(result);
    }

    @Override
    public void deleteAccount(Long id) {
        this.accountRepository.deleteById(id);

    }

    @Override
    public List<AccountDto> getAccounts(Long organizationId) {
        return this.accountRepository.findByOrganizationId(organizationId)
                .stream()
                .map(accountDtoMapper)
                .toList();
    }
}
