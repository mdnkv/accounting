package dev.mednikov.accounting.transactions.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.accounts.models.Account;
import dev.mednikov.accounting.accounts.models.AccountType;
import dev.mednikov.accounting.accounts.repositories.AccountRepository;
import dev.mednikov.accounting.currencies.models.Currency;
import dev.mednikov.accounting.currencies.repositories.CurrencyRepository;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import dev.mednikov.accounting.transactions.dto.TransactionDto;
import dev.mednikov.accounting.transactions.dto.TransactionLineDto;
import dev.mednikov.accounting.transactions.exceptions.TransactionDeletionException;
import dev.mednikov.accounting.transactions.exceptions.UnbalancedTransactionException;
import dev.mednikov.accounting.transactions.models.Transaction;
import dev.mednikov.accounting.transactions.models.TransactionLine;
import dev.mednikov.accounting.transactions.repositories.TransactionLineRepository;
import dev.mednikov.accounting.transactions.repositories.TransactionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    private final SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Mock private TransactionRepository transactionRepository;
    @Mock private AccountRepository accountRepository;
    @Mock private TransactionLineRepository transactionLineRepository;
    @Mock private CurrencyRepository currencyRepository;
    @Mock private OrganizationRepository organizationRepository;
    @Mock private ApplicationEventPublisher eventPublisher;

    @InjectMocks private TransactionServiceImpl transactionService;

    @Test
    void createTransaction_unbalancedTest(){
        // Generate ids
        Long organizationId = snowflakeGenerator.next();
        Long accountId1 = snowflakeGenerator.next();
        Long accountId2 = snowflakeGenerator.next();

        // Create lines
        List<TransactionLineDto> payloadLines = new ArrayList<>();
        TransactionLineDto transactionLineDto1 = new TransactionLineDto();
        transactionLineDto1.setCreditAmount(BigDecimal.valueOf(1000.00));
        transactionLineDto1.setDebitAmount(BigDecimal.ZERO);
        transactionLineDto1.setAccountId(accountId1.toString());
        payloadLines.add(transactionLineDto1);
        TransactionLineDto transactionLineDto2 = new TransactionLineDto();
        transactionLineDto2.setCreditAmount(BigDecimal.ZERO);
        transactionLineDto2.setDebitAmount(BigDecimal.valueOf(500.00));
        transactionLineDto2.setAccountId(accountId2.toString());
        payloadLines.add(transactionLineDto2);

        // Create payload
        TransactionDto payload = new TransactionDto();
        payload.setOrganizationId(organizationId.toString());
        payload.setLines(payloadLines);
        payload.setDescription("Aliquam vulputate, dolor non mollis rhoncus");
        payload.setCurrencyId(snowflakeGenerator.next().toString());
        payload.setDate(LocalDate.now().minusDays(7));

        // Execute the method
        Assertions.assertThatThrownBy(() -> transactionService.createTransaction(payload)).isInstanceOf(UnbalancedTransactionException.class);
    }

    @Test
    void createTransaction_successTest(){
        // Create an organization
        Long organizationId = snowflakeGenerator.next();
        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Schade Bader AG & Co. KG");

        // Create a currency
        Long currencyId = snowflakeGenerator.next();
        Currency currency = new Currency();
        currency.setId(currencyId);
        currency.setName("EUR - Euro");
        currency.setCode("EUR");
        currency.setOrganization(organization);
        currency.setPrimary(true);

        // Create a debited account
        Long debitAccountId = snowflakeGenerator.next();
        Account debitAccount = new Account();
        debitAccount.setId(debitAccountId);
        debitAccount.setCode("17300");
        debitAccount.setName("Equipment");
        debitAccount.setAccountType(AccountType.ASSET);
        debitAccount.setOrganization(organization);
        debitAccount.setDeprecated(false);

        // Create a credited account
        Long creditAccountId = snowflakeGenerator.next();
        Account creditAccount = new Account();
        creditAccount.setId(creditAccountId);
        creditAccount.setCode("17300");
        creditAccount.setName("Cash");
        creditAccount.setAccountType(AccountType.ASSET);
        creditAccount.setOrganization(organization);
        creditAccount.setDeprecated(false);

        // Create a transaction
        Long transactionId = snowflakeGenerator.next();
        Transaction transaction = new Transaction();
        transaction.setOrganization(organization);
        transaction.setBaseCurrency(currency);
        transaction.setTargetCurrency(currency);
        transaction.setDate(LocalDate.now().minusDays(10));
        transaction.setDescription("Aliquam mi leo, mattis a rhoncus eu, hendrerit vitae odio.");
        transaction.setId(transactionId);

        // Create transaction lines
        TransactionLine line1 = new TransactionLine();
        line1.setAccount(debitAccount);
        line1.setDebitAmount(BigDecimal.valueOf(1000.00));
        line1.setCreditAmount(BigDecimal.ZERO);
        line1.setId(snowflakeGenerator.next());
        line1.setTransaction(transaction);
        transaction.addTransactionLine(line1);

        TransactionLine line2 = new TransactionLine();
        line2.setAccount(creditAccount);
        line2.setDebitAmount(BigDecimal.ZERO);
        line2.setCreditAmount(BigDecimal.valueOf(1000.00));
        line2.setId(snowflakeGenerator.next());
        line2.setTransaction(transaction);
        transaction.addTransactionLine(line2);

        // Create payload
        List<TransactionLineDto> payloadLines = new ArrayList<>();
        TransactionLineDto transactionLineDto1 = new TransactionLineDto();
        transactionLineDto1.setCreditAmount(BigDecimal.valueOf(1000.00));
        transactionLineDto1.setDebitAmount(BigDecimal.ZERO);
        transactionLineDto1.setAccountId(debitAccountId.toString());
        payloadLines.add(transactionLineDto1);
        TransactionLineDto transactionLineDto2 = new TransactionLineDto();
        transactionLineDto2.setCreditAmount(BigDecimal.ZERO);
        transactionLineDto2.setDebitAmount(BigDecimal.valueOf(1000.00));
        transactionLineDto2.setAccountId(creditAccountId.toString());
        payloadLines.add(transactionLineDto2);
        TransactionDto payload = new TransactionDto();
        payload.setLines(payloadLines);
        payload.setCurrencyId(currencyId.toString());
        payload.setDate(LocalDate.now().minusDays(10));
        payload.setOrganizationId(organizationId.toString());
        payload.setDescription("Aliquam mi leo, mattis a rhoncus eu, hendrerit vitae odio.");

        Mockito.when(organizationRepository.getReferenceById(organizationId)).thenReturn(organization);
        Mockito.when(currencyRepository.findById(currencyId)).thenReturn(Optional.of(currency));
        Mockito.when(currencyRepository.findPrimaryCurrency(organizationId)).thenReturn(Optional.of(currency));
        Mockito.when(accountRepository.findById(debitAccountId)).thenReturn(Optional.of(debitAccount));
        Mockito.when(accountRepository.findById(creditAccountId)).thenReturn(Optional.of(creditAccount));
        Mockito.when(transactionLineRepository.saveAll(Mockito.any())).thenReturn(List.of(line1, line2));
        Mockito.when(transactionRepository.save(transaction)).thenReturn(transaction);

        TransactionDto result = transactionService.createTransaction(payload);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void deleteTransaction_isNotDraftTest(){
        Long transactionId = snowflakeGenerator.next();
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setDraft(false);

        Mockito.when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        Assertions.assertThatThrownBy(() -> transactionService.deleteTransaction(transactionId)).isInstanceOf(TransactionDeletionException.class);
    }

}
