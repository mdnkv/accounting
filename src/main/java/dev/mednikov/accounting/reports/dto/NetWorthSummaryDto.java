package dev.mednikov.accounting.reports.dto;

import java.math.BigDecimal;

public record NetWorthSummaryDto(
        String currency,
        BigDecimal totalAssets,
        BigDecimal totalLiabilities,
        BigDecimal netWorth
) {
}
