package dev.mednikov.accounting.reports.dto;

import java.math.BigDecimal;

public record ExpenseCategoryDto (
        String name,
        BigDecimal amount
) {
}
