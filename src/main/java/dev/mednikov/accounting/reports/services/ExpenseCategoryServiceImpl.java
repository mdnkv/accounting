package dev.mednikov.accounting.reports.services;

import dev.mednikov.accounting.accounts.models.Account;
import dev.mednikov.accounting.reports.dto.ExpenseCategoryDto;
import dev.mednikov.accounting.reports.utils.ExpenseCategoryComparator;
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
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService {

    private final TransactionLineRepository transactionLineRepository;

    public ExpenseCategoryServiceImpl(TransactionLineRepository transactionLineRepository) {
        this.transactionLineRepository = transactionLineRepository;
    }

    @Override
    public List<ExpenseCategoryDto> getExpenseCategories(Long organizationId, int daysCount) {
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = toDate.minusDays(daysCount);
        List<TransactionLine> transactionLines = this.transactionLineRepository.getExpenseLines(organizationId, fromDate, toDate);
        Map<Account, List<TransactionLine>> accounts = transactionLines.stream().collect(Collectors.groupingBy(TransactionLine::getAccount));
        List<ExpenseCategoryDto> expenseCategories = new ArrayList<>();
        for (Map.Entry<Account, List<TransactionLine>> entry : accounts.entrySet()) {
            BigDecimal debit = entry.getValue().stream()
                    .map(TransactionLine::getDebitAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal credit = entry.getValue().stream()
                    .map(TransactionLine::getCreditAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal amount = debit.subtract(credit);
            ExpenseCategoryDto categoryDto = new ExpenseCategoryDto(entry.getKey().getName(), amount);
            expenseCategories.add(categoryDto);
        }
        ExpenseCategoryComparator comparator = new ExpenseCategoryComparator();
        List<ExpenseCategoryDto> result = expenseCategories.stream()
                .sorted(comparator)
                .limit(5)
                .toList();
        return expenseCategories;
    }

}
