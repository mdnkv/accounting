package dev.mednikov.accounting.transactions.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.accounts.exceptions.AccountNotFoundException;
import dev.mednikov.accounting.accounts.exceptions.DeprecatedAccountException;
import dev.mednikov.accounting.accounts.models.Account;
import dev.mednikov.accounting.accounts.repositories.AccountRepository;
import dev.mednikov.accounting.currencies.exceptions.CurrencyNotFoundException;
import dev.mednikov.accounting.currencies.exceptions.DeprecatedCurrencyException;
import dev.mednikov.accounting.currencies.models.Currency;
import dev.mednikov.accounting.currencies.repositories.CurrencyRepository;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import dev.mednikov.accounting.transactions.dto.TransactionDto;
import dev.mednikov.accounting.transactions.dto.TransactionDtoMapper;
import dev.mednikov.accounting.transactions.dto.TransactionLineDto;
import dev.mednikov.accounting.transactions.exceptions.TransactionDeletionException;
import dev.mednikov.accounting.transactions.exceptions.UnbalancedTransactionException;
import dev.mednikov.accounting.transactions.models.Transaction;
import dev.mednikov.accounting.transactions.models.TransactionLine;
import dev.mednikov.accounting.transactions.repositories.TransactionLineRepository;
import dev.mednikov.accounting.transactions.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
    private final static TransactionDtoMapper transactionDtoMapper = new TransactionDtoMapper();

    private final CurrencyRepository currencyRepository;
    private final OrganizationRepository organizationRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionLineRepository transactionLineRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(OrganizationRepository organizationRepository,
                                  TransactionRepository transactionRepository,
                                  TransactionLineRepository transactionLineRepository,
                                  CurrencyRepository currencyRepository,
                                  AccountRepository accountRepository) {
        this.organizationRepository = organizationRepository;
        this.transactionRepository = transactionRepository;
        this.transactionLineRepository = transactionLineRepository;
        this.accountRepository = accountRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public TransactionDto createTransaction(TransactionDto payload) {
        // Check that transaction has at least 2 lines
        if (payload.getLines().size() < 2){
            throw new UnbalancedTransactionException();
        }
        // Verify that credit == debit
        BigDecimal creditAmount = payload.getLines().stream()
                .map(TransactionLineDto::getCreditAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal debitAmount = payload.getLines().stream()
                .map(TransactionLineDto::getDebitAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (!creditAmount.equals(debitAmount)) {
            throw new UnbalancedTransactionException();
        }
        // Get organization
        Long organizationId = Long.valueOf(payload.getOrganizationId());
        Organization organization = this.organizationRepository.getReferenceById(organizationId);
        // Get currency
        Long currencyId = Long.valueOf(payload.getCurrencyId());
        Currency currency = this.currencyRepository.findById(currencyId).orElseThrow(CurrencyNotFoundException::new);
        if (currency.isDeprecated()){
            throw new DeprecatedCurrencyException();
        }
        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setId(snowflakeGenerator.next());
        transaction.setOrganization(organization);
        transaction.setDescription(payload.getDescription());
        transaction.setDate(payload.getDate());
        transaction.setCurrency(currency);
        transaction.setDraft(true);
        // Persist transaction to the datasource
        Transaction transactionResult = this.transactionRepository.save(transaction);
        // Process transaction lines
        List<TransactionLine> lines = new ArrayList<>();
        for (TransactionLineDto lineDto: payload.getLines()) {
            Long accountId = Long.valueOf(lineDto.getAccountId());
            Account account = this.accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
            if (account.isDeprecated()){
                throw new DeprecatedAccountException();
            }
            // Create transaction line
            TransactionLine line = new TransactionLine();
            line.setAccount(account);
            line.setTransaction(transaction);
            line.setCreditAmount(lineDto.getCreditAmount());
            line.setDebitAmount(lineDto.getDebitAmount());
            line.setId(snowflakeGenerator.next());
            // Add to list
            lines.add(line);
        }
        // Attach lines to the transaction entity
        List<TransactionLine> linesResult = this.transactionLineRepository.saveAll(lines);
        transactionResult.setLines(linesResult);
        transactionResult.setDraft(false);
        // Save transaction with lines
        Transaction result = this.transactionRepository.save(transactionResult);
        return transactionDtoMapper.apply(result);
    }

    @Override
    public List<TransactionDto> getTransactions(Long organizationId) {
        return this.transactionRepository
                .findAllByOrganizationId(organizationId)
                .stream()
                .map(transactionDtoMapper)
                .toList();
    }

    @Override
    public void deleteTransaction(Long transactionId) {
        // Only draft transaction can be removed
        Optional<Transaction> result = this.transactionRepository.findById(transactionId);
        if (result.isPresent()) {
            Transaction transaction = result.get();
            if (transaction.isDraft()){
                this.transactionRepository.delete(transaction);
            } else {
                throw new TransactionDeletionException();
            }
        }
    }
}
