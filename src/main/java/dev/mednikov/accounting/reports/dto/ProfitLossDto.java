package dev.mednikov.accounting.reports.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProfitLossDto(
        String organizationName,
        List<ProfitLossLineDto> expenseItems,
        List<ProfitLossLineDto> incomeItems,
        BigDecimal netIncome
) {
}
