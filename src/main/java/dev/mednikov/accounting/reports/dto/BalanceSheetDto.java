package dev.mednikov.accounting.reports.dto;

import dev.mednikov.accounting.currencies.dto.CurrencyDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BalanceSheetDto {

    private CurrencyDto currency;
    private LocalDate date;
    private  List<BalanceSheetLineDto> items;
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private BigDecimal totalEquity;
    private boolean balanced;

    public CurrencyDto getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyDto currency) {
        this.currency = currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<BalanceSheetLineDto> getItems() {
        return items;
    }

    public void setItems(List<BalanceSheetLineDto> items) {
        this.items = items;
    }

    public boolean isBalanced() {
        return balanced;
    }

    public void setBalanced(boolean balanced) {
        this.balanced = balanced;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public BigDecimal getTotalLiabilities() {
        return totalLiabilities;
    }

    public void setTotalLiabilities(BigDecimal totalLiabilities) {
        this.totalLiabilities = totalLiabilities;
    }

    public BigDecimal getTotalEquity() {
        return totalEquity;
    }

    public void setTotalEquity(BigDecimal totalEquity) {
        this.totalEquity = totalEquity;
    }
}
