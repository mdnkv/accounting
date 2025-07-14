package dev.mednikov.accounting.reports.services;

import dev.mednikov.accounting.accounts.dto.AccountDto;
import dev.mednikov.accounting.accounts.dto.AccountDtoMapper;
import dev.mednikov.accounting.accounts.models.Account;
import dev.mednikov.accounting.accounts.models.AccountType;
import dev.mednikov.accounting.organizations.exceptions.OrganizationNotFoundException;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import dev.mednikov.accounting.reports.dto.ProfitLossDto;
import dev.mednikov.accounting.reports.dto.ProfitLossLineDto;
import dev.mednikov.accounting.reports.dto.ProfitLossSummaryDto;
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
public class ProfitLossServiceImpl implements ProfitLossService {

    private final static AccountDtoMapper accountDtoMapper = new AccountDtoMapper();

    private final OrganizationRepository organizationRepository;
    private final TransactionLineRepository transactionLineRepository;

    public ProfitLossServiceImpl(OrganizationRepository organizationRepository, TransactionLineRepository transactionLineRepository) {
        this.organizationRepository = organizationRepository;
        this.transactionLineRepository = transactionLineRepository;
    }

    @Override
    public ProfitLossDto getProfitLoss(Long organizationId, LocalDate fromDate, LocalDate toDate) {
        // Get organization
        Organization organization = this.organizationRepository.findById(organizationId).orElseThrow(OrganizationNotFoundException::new);

        // Get transaction lines
        List<TransactionLine> transactionLines = this.transactionLineRepository.getProfitLossLines(organizationId, fromDate, toDate);

        // Group transactions by account
        Map<Account, List<TransactionLine>> accounts = transactionLines.stream().collect(Collectors.groupingBy(TransactionLine::getAccount));

        // Calculate amount in each account
        List<ProfitLossLineDto> incomeItems = new ArrayList<>();
        List<ProfitLossLineDto> expenseItems = new ArrayList<>();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (Map.Entry<Account, List<TransactionLine>> entry : accounts.entrySet()) {
            AccountDto account = accountDtoMapper.apply(entry.getKey());
            BigDecimal amount = BigDecimal.ZERO;
            // Calculate amount for the account
            BigDecimal debit = entry.getValue().stream()
                    .map(TransactionLine::getDebitAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal credit = entry.getValue().stream()
                    .map(TransactionLine::getCreditAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal balance = (account.getAccountType() == AccountType.EXPENSE) ? debit.subtract(credit) : credit.subtract(amount);
            ProfitLossLineDto dto = new ProfitLossLineDto(account, balance);
            if (account.getAccountType() == AccountType.EXPENSE) {
                expenseItems.add(dto);
                totalExpense = totalExpense.add(balance);
            } else {
                incomeItems.add(dto);
                totalIncome = totalIncome.add(balance);
            }
        }

        BigDecimal netIncome = totalIncome.subtract(totalExpense);

        return new ProfitLossDto(
                organization.getName(),
                expenseItems,
                incomeItems,
                netIncome
        );
    }

    @Override
    public ProfitLossSummaryDto getProfitLossSummary(Long organizationId, int daysCount) {
        Organization organization = this.organizationRepository.findById(organizationId).orElseThrow(OrganizationNotFoundException::new);

        // Calculate dates
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = toDate.minusDays(daysCount);

        List<TransactionLine> transactionLines = this.transactionLineRepository.getProfitLossLines(organizationId, fromDate, toDate);

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (TransactionLine transactionLine : transactionLines) {
            if (transactionLine.getAccount().getAccountType() == AccountType.INCOME) {
                BigDecimal income = transactionLine.getCreditAmount().subtract(transactionLine.getDebitAmount());
                totalIncome = totalIncome.add(income);
            } else if (transactionLine.getAccount().getAccountType() == AccountType.EXPENSE) {
                BigDecimal expense = transactionLine.getDebitAmount().subtract(transactionLine.getCreditAmount());
                totalExpense = totalExpense.add(expense);
            }
        }

        BigDecimal netProfit = totalIncome.subtract(totalExpense);

        return new ProfitLossSummaryDto(
                totalIncome,
                totalExpense,
                netProfit
        );
    }
}
