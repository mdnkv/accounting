package dev.mednikov.accounting.reports.dto;

import java.math.BigDecimal;
import java.util.List;

public record BalanceSheetDto(
        String organizationName,
        String currency,
        List<BalanceSheetLineDto> items,
        BigDecimal totalCreditAmount,
        BigDecimal totalDebitAmount,
        boolean balanced
) {
}
