package dev.mednikov.accounting.transactions.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class TransactionDto {

    private String id;
    private String organizationId;
    private String description;
    private String currency;
    private LocalDate date;
    private BigDecimal totalCreditAmount;
    private BigDecimal totalDebitAmount;
    private List<TransactionLineDto> lines;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<TransactionLineDto> getLines() {
        return lines;
    }

    public void setLines(List<TransactionLineDto> lines) {
        this.lines = lines;
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
}
