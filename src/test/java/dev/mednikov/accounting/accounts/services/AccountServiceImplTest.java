package dev.mednikov.accounting.accounts.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.accounts.dto.AccountDto;
import dev.mednikov.accounting.accounts.exceptions.AccountAlreadyExistsException;
import dev.mednikov.accounting.accounts.exceptions.AccountDeletionException;
import dev.mednikov.accounting.accounts.exceptions.AccountNotFoundException;
import dev.mednikov.accounting.accounts.models.Account;
import dev.mednikov.accounting.accounts.models.AccountType;
import dev.mednikov.accounting.accounts.repositories.AccountRepository;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import dev.mednikov.accounting.transactions.models.TransactionLine;
import dev.mednikov.accounting.transactions.repositories.TransactionLineRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Mock private AccountRepository accountRepository;
    @Mock private OrganizationRepository organizationRepository;
    @Mock private TransactionLineRepository transactionLineRepository;
    @InjectMocks private AccountServiceImpl accountService;

    @Test
    void createAccount_alreadyExistsTest(){
        Long accountId = snowflakeGenerator.next();
        String accountCode = "10100";
        Long organizationId = snowflakeGenerator.next();
        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Jäger AG & Co. KGaA");
        organization.setCurrency("EUR");

        Account account = new Account();
        account.setId(accountId);
        account.setOrganization(organization);
        account.setAccountType(AccountType.ASSET);
        account.setName("Cash – Regular Checking");
        account.setCode(accountCode);

        AccountDto payload = new AccountDto();
        payload.setOrganizationId(organizationId.toString());
        payload.setAccountType(AccountType.ASSET);
        payload.setName("Cash - Regular Checking");
        payload.setCode(accountCode);

        Mockito.when(accountRepository.findByOrganizationIdAndCode(organizationId, accountCode)).thenReturn(Optional.of(account));
        Assertions.assertThatThrownBy(() -> accountService.createAccount(payload)).isInstanceOf(AccountAlreadyExistsException.class);
    }

    @Test
    void createAccount_successTest(){
        Long accountId = snowflakeGenerator.next();
        String accountCode = "10100";
        Long organizationId = snowflakeGenerator.next();
        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Reichert AG & Co. KG");
        organization.setCurrency("EUR");

        Account account = new Account();
        account.setId(accountId);
        account.setOrganization(organization);
        account.setAccountType(AccountType.ASSET);
        account.setName("Cash – Regular Checking");
        account.setCode(accountCode);

        AccountDto payload = new AccountDto();
        payload.setOrganizationId(organizationId.toString());
        payload.setAccountType(AccountType.ASSET);
        payload.setName("Cash - Regular Checking");
        payload.setCode(accountCode);

        Mockito.when(accountRepository.findByOrganizationIdAndCode(organizationId, accountCode)).thenReturn(Optional.empty());
        Mockito.when(organizationRepository.getReferenceById(organizationId)).thenReturn(organization);
        Mockito.when(accountRepository.save(account)).thenReturn(account);

        AccountDto result = accountService.createAccount(payload);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void updateAccount_notFoundTest(){
        Long accountId = snowflakeGenerator.next();
        String accountCode = "12500";
        Long organizationId = snowflakeGenerator.next();

        AccountDto payload = new AccountDto();
        payload.setOrganizationId(organizationId.toString());
        payload.setAccountType(AccountType.ASSET);
        payload.setName("Allowance for Doubtful Accounts");
        payload.setCode(accountCode);
        payload.setId(accountId.toString());

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> accountService.updateAccount(payload)).isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void updateAccount_alreadyExistsTest(){
        Long accountId = snowflakeGenerator.next();
        String accountCode = "17000";
        Long organizationId = snowflakeGenerator.next();

        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Springer Popp GmbH");
        organization.setCurrency("EUR");

        AccountDto payload = new AccountDto();
        payload.setOrganizationId(organizationId.toString());
        payload.setAccountType(AccountType.ASSET);
        payload.setName("Allowance for Doubtful Accounts");
        payload.setCode(accountCode);
        payload.setId(accountId.toString());

        Account account = new Account();
        account.setId(accountId);
        account.setOrganization(organization);
        account.setAccountType(AccountType.ASSET);
        account.setName("Allowance for Doubtful Accounts");
        account.setCode("12500");

        Account anotherAccount = new Account();
        anotherAccount.setId(snowflakeGenerator.next());
        anotherAccount.setAccountType(AccountType.ASSET);
        anotherAccount.setCode(accountCode);
        anotherAccount.setOrganization(organization);
        anotherAccount.setName("Land");

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.findByOrganizationIdAndCode(organizationId, accountCode)).thenReturn(Optional.of(anotherAccount));

        Assertions.assertThatThrownBy(() -> accountService.updateAccount(payload)).isInstanceOf(AccountAlreadyExistsException.class);
    }

    @Test
    void updateAccount_successTest(){
        Long accountId = snowflakeGenerator.next();
        String accountCode = "20100";
        Long organizationId = snowflakeGenerator.next();

        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Breuer GmbH & Co. KG");
        organization.setCurrency("EUR");

        AccountDto payload = new AccountDto();
        payload.setOrganizationId(organizationId.toString());
        payload.setAccountType(AccountType.ASSET);
        payload.setName("Notes Payable");
        payload.setCode(accountCode);
        payload.setId(accountId.toString());

        Account account = new Account();
        account.setId(accountId);
        account.setOrganization(organization);
        account.setAccountType(AccountType.ASSET);
        account.setName("Notes Payable");
        account.setCode(accountCode);
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        AccountDto result = accountService.updateAccount(payload);
        Assertions.assertThat(result).isNotNull();

    }

    @Test
    void getAllAccountsTest(){
        Long organizationId = snowflakeGenerator.next();
        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Fiedler GmbH & Co. KGaA");
        organization.setCurrency("EUR");

        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setId(snowflakeGenerator.next());
        account1.setAccountType(AccountType.LIABILITY);
        account1.setOrganization(organization);
        account1.setName("Notes Payable – Credit Line #2");
        account1.setCode("20200");
        account1.setDeprecated(false);
        accounts.add(account1);

        Account account2 = new Account();
        account2.setId(snowflakeGenerator.next());
        account2.setAccountType(AccountType.INCOME);
        account2.setOrganization(organization);
        account2.setName("Revenue");
        account2.setCode("31010");
        account2.setDeprecated(false);
        accounts.add(account2);

        Mockito.when(accountRepository.findAllByOrganizationId(organizationId)).thenReturn(accounts);
        List<AccountDto> result = accountService.getAccounts(organizationId, true);
        Assertions.assertThat(result).isNotNull().hasSize(2);
    }

    @Test
    void getActiveAccountsTest(){
        Long organizationId = snowflakeGenerator.next();
        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Ackermann Oswald AG");
        organization.setCurrency("EUR");

        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setId(snowflakeGenerator.next());
        account1.setAccountType(AccountType.LIABILITY);
        account1.setOrganization(organization);
        account1.setName("Notes Payable – Credit Line #2");
        account1.setCode("20200");
        account1.setDeprecated(true);
        accounts.add(account1);

        Mockito.when(accountRepository.findActiveByOrganizationId(organizationId)).thenReturn(accounts);
        List<AccountDto> result = accountService.getAccounts(organizationId, false);
        Assertions.assertThat(result).isNotNull().hasSize(1);
    }

    @Test
    void getAccount_existsTest(){
        Long accountId = snowflakeGenerator.next();
        Long organizationId = snowflakeGenerator.next();
        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Gottschalk Metzger OHG mbH");
        organization.setCurrency("EUR");

        Account account = new Account();
        account.setId(accountId);
        account.setAccountType(AccountType.LIABILITY);
        account.setOrganization(organization);
        account.setName("Notes Payable – Credit Line #2");
        account.setCode("20200");

        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        Optional<AccountDto> result = accountService.getAccount(accountId);
        Assertions.assertThat(result).isPresent();
    }

    @Test
    void getAccount_notExistsTest(){
        Long accountId = snowflakeGenerator.next();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        Optional<AccountDto> result = accountService.getAccount(accountId);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void deleteAccountWithTransactionsTest(){
        Long accountId = snowflakeGenerator.next();
        TransactionLine transactionLine = new TransactionLine();
        Mockito.when(transactionLineRepository.findByAccountId(accountId)).thenReturn(List.of(transactionLine));
        Assertions
                .assertThatThrownBy(() -> accountService.deleteAccount(accountId))
                .isInstanceOf(AccountDeletionException.class);
    }

}
