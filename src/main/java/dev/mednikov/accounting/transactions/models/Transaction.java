package dev.mednikov.accounting.transactions.models;

import dev.mednikov.accounting.currencies.models.Currency;
import dev.mednikov.accounting.organizations.models.Organization;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Organization organization;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Currency currency;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "transaction_date")
    private LocalDate date;

    @Column(nullable = false, name = "is_draft")
    private boolean draft;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TransactionLine> lines = new HashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Transaction that)) return false;

        return organization.equals(that.organization)
                && currency.equals(that.currency)
                && description.equals(that.description)
                && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        int result = organization.hashCode();
        result = 31 * result + currency.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public void setLines(List<TransactionLine> lines) {
        this.lines.addAll(lines);
    }

    public void addTransactionLine(TransactionLine transactionLine) {
        this.lines.add(transactionLine);
    }

    public Set<TransactionLine> getLines() {
        return lines;
    }

}
