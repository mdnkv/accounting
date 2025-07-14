package dev.mednikov.accounting.reports.dto;

import java.math.BigDecimal;

public record NetWorthSummaryDto(
        BigDecimal totalAssets,
        BigDecimal totalLiabilities,
        BigDecimal netWorth
) {
}
