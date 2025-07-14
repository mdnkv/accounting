package dev.mednikov.accounting.reports.dto;

import java.math.BigDecimal;

public record ProfitLossSummaryDto(
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal netProfit
) {
}
