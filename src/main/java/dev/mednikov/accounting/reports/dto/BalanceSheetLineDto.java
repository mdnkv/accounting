package dev.mednikov.accounting.reports.dto;

import dev.mednikov.accounting.accounts.dto.AccountDto;

import java.math.BigDecimal;

public record BalanceSheetLineDto(
        AccountDto account,
        BigDecimal creditAmount,
        BigDecimal debitAmount
) {
}
