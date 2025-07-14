package dev.mednikov.accounting.transactions.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.accounts.models.Account;
import dev.mednikov.accounting.accounts.repositories.AccountRepository;
import dev.mednikov.accounting.currencies.models.Currency;
import dev.mednikov.accounting.currencies.repositories.CurrencyRepository;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import dev.mednikov.accounting.transactions.dto.TransactionDto;
import dev.mednikov.accounting.transactions.dto.TransactionDtoMapper;
import dev.mednikov.accounting.transactions.dto.TransactionLineDto;
import dev.mednikov.accounting.transactions.exceptions.UnbalancedTransactionException;
import dev.mednikov.accounting.transactions.models.Transaction;
import dev.mednikov.accounting.transactions.models.TransactionLine;
import dev.mednikov.accounting.transactions.repositories.TransactionLineRepository;
import dev.mednikov.accounting.transactions.repositories.TransactionRepository;
import dev.mednikov.accounting.users.models.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        Currency currency = this.currencyRepository.getReferenceById(currencyId);

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setId(snowflakeGenerator.next());
        transaction.setOrganization(organization);
        transaction.setDescription(payload.getDescription());
        transaction.setDate(payload.getDate());
        transaction.setCurrency(currency);

        // Persist transaction to the datasource
        Transaction transactionResult = this.transactionRepository.save(transaction);

        // Process transaction lines
        List<TransactionLine> lines = new ArrayList<>();
        for (TransactionLineDto lineDto: payload.getLines()) {
            TransactionLine line = new TransactionLine();
            line.setTransaction(transaction);
            line.setCreditAmount(lineDto.getCreditAmount());
            line.setDebitAmount(lineDto.getDebitAmount());
            line.setId(snowflakeGenerator.next());

            Long accountId = Long.valueOf(lineDto.getAccountId());
            Account account = this.accountRepository.getReferenceById(accountId);
            line.setAccount(account);
            lines.add(line);
        }

        // Attach lines to the transaction entity
        List<TransactionLine> linesResult = this.transactionLineRepository.saveAll(lines);
        transactionResult.setLines(linesResult);

        // Save transaction with lines
        Transaction result = this.transactionRepository.save(transactionResult);
        return transactionDtoMapper.apply(result);

    }

    @Override
    public List<TransactionDto> getTransactions(Long organizationId) {
        return this.transactionRepository.findAllByOrganizationId(organizationId)
                .stream().map(transactionDtoMapper).toList();
    }

}
