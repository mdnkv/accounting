package dev.mednikov.accounting.transactions.dto;

import dev.mednikov.accounting.currencies.dto.CurrencyDto;
import dev.mednikov.accounting.currencies.dto.CurrencyDtoMapper;
import dev.mednikov.accounting.transactions.models.Transaction;
import dev.mednikov.accounting.transactions.models.TransactionLine;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

public final class TransactionDtoMapper implements Function<Transaction, TransactionDto> {

    private final static CurrencyDtoMapper currencyDtoMapper = new CurrencyDtoMapper();
    private final static TransactionLineDtoMapper transactionLineDtoMapper = new TransactionLineDtoMapper();

    @Override
    public TransactionDto apply(Transaction transaction) {
        CurrencyDto currencyDto = currencyDtoMapper.apply(transaction.getCurrency());
        List<TransactionLineDto> lines = transaction.getLines().stream().map(transactionLineDtoMapper).toList();
        BigDecimal totalCredit = transaction.getLines().stream().map(TransactionLine::getCreditAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDebit = transaction.getLines().stream().map(TransactionLine::getDebitAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        TransactionDto result = new TransactionDto();
        result.setId(transaction.getId().toString());
        result.setOrganizationId(transaction.getOrganization().getId().toString());
        result.setDescription(transaction.getDescription());
        result.setDate(transaction.getDate());
        result.setCurrencyId(currencyDto.getId());
        result.setCurrency(currencyDto);
        result.setLines(lines);
        result.setTotalCreditAmount(totalCredit);
        result.setTotalDebitAmount(totalDebit);
        return result;
    }

}
