package dev.mednikov.accounting.reports.services;

import dev.mednikov.accounting.accounts.dto.AccountDto;
import dev.mednikov.accounting.accounts.dto.AccountDtoMapper;
import dev.mednikov.accounting.accounts.models.Account;
import dev.mednikov.accounting.currencies.exceptions.CurrencyNotFoundException;
import dev.mednikov.accounting.currencies.models.Currency;
import dev.mednikov.accounting.currencies.repositories.CurrencyRepository;
import dev.mednikov.accounting.organizations.exceptions.OrganizationNotFoundException;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import dev.mednikov.accounting.reports.dto.BalanceSheetDto;
import dev.mednikov.accounting.reports.dto.BalanceSheetLineDto;
import dev.mednikov.accounting.transactions.models.TransactionLine;
import dev.mednikov.accounting.transactions.repositories.TransactionLineRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BalanceSheetServiceImpl implements BalanceSheetService {

    private final static AccountDtoMapper accountDtoMapper = new AccountDtoMapper();

    private final TransactionLineRepository transactionLineRepository;
    private final OrganizationRepository organizationRepository;
    private final CurrencyRepository currencyRepository;

    public BalanceSheetServiceImpl(TransactionLineRepository transactionLineRepository, OrganizationRepository organizationRepository, CurrencyRepository currencyRepository) {
        this.transactionLineRepository = transactionLineRepository;
        this.organizationRepository = organizationRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public BalanceSheetDto getBalanceSheet(Long organizationId, LocalDate date) {
        // Retrieve organization
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(OrganizationNotFoundException::new);

        // Retrieve primary currency
        Currency primaryCurrency = this.currencyRepository.findPrimaryCurrency(organizationId).orElseThrow(CurrencyNotFoundException::new);

        // Retrieve transactions
        List<TransactionLine> transactionLines = this.transactionLineRepository.getBalanceSheetLines(organizationId, date);

        // Group transactions by account
        Map<Account, List<TransactionLine>> accounts = transactionLines.stream().collect(Collectors.groupingBy(TransactionLine::getAccount));
        BigDecimal totalCreditAmount = BigDecimal.ZERO;
        BigDecimal totalDebitAmount = BigDecimal.ZERO;

        // Calculate debit and credit for each account and produce BalanceSheetItems
        List<BalanceSheetLineDto> items = new ArrayList<>();
        for (Map.Entry<Account, List<TransactionLine>> accountEntry : accounts.entrySet()) {
            BigDecimal debit = BigDecimal.ZERO;
            BigDecimal credit = BigDecimal.ZERO;

            for (TransactionLine transactionLine : accountEntry.getValue()) {
                if (!transactionLine.getTransaction().isDraft()){
                    if (transactionLine.getTransaction().getTargetCurrency().equals(primaryCurrency)) {
                        // same currency as primary currency
                        // add an original amount
                        debit = debit.add(transactionLine.getOriginalDebitAmount());
                        credit = credit.add(transactionLine.getOriginalCreditAmount());
                    } else {
                        // another currency
                        // add a converted amount
                        credit = credit.add(transactionLine.getCreditAmount());
                        debit = debit.add(transactionLine.getDebitAmount());
                    }
                }

            }
//
//            // Calculate debit
//            BigDecimal debit = accountEntry.getValue().stream()
//                    .map(TransactionLine::getDebitAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//            totalDebitAmount = totalDebitAmount.add(debit);
//
//            // Calculate credit
//            BigDecimal credit = accountEntry.getValue().stream()
//                    .map(TransactionLine::getCreditAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//            totalCreditAmount = totalCreditAmount.add(credit);

            totalDebitAmount = totalDebitAmount.add(debit);
            totalCreditAmount = totalCreditAmount.add(credit);
            // Map account to dto
            AccountDto account = accountDtoMapper.apply(accountEntry.getKey());
            // Create balance sheet line dto
            BalanceSheetLineDto item = new BalanceSheetLineDto(account, credit, debit);
            items.add(item);
        }

        boolean balanced = totalCreditAmount.equals(totalDebitAmount);
        return new BalanceSheetDto(
                organization.getName(),
                items,
                totalCreditAmount,
                totalDebitAmount
        );
    }
}
