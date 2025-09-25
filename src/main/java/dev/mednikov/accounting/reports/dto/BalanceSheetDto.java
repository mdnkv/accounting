package dev.mednikov.accounting.reports.dto;

import dev.mednikov.accounting.currencies.dto.CurrencyDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BalanceSheetDto {

    private CurrencyDto currency;
    private LocalDate date;
    private  List<BalanceSheetLineDto> items;
    private BigDecimal totalCreditAmount;
    private BigDecimal totalDebitAmount;
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

    public BigDecimal getTotalCreditAmount() {
        return totalCreditAmount;
    }

    public void setTotalCreditAmount(BigDecimal totalCreditAmount) {
        this.totalCreditAmount = totalCreditAmount;
    }

    public BigDecimal getTotalDebitAmount() {
        return totalDebitAmount;
    }

    public void setTotalDebitAmount(BigDecimal totalDebitAmount) {
        this.totalDebitAmount = totalDebitAmount;
    }

    public boolean isBalanced() {
        return balanced;
    }

    public void setBalanced(boolean balanced) {
        this.balanced = balanced;
    }
}
