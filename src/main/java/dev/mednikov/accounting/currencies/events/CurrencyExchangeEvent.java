package dev.mednikov.accounting.currencies.events;

import dev.mednikov.accounting.transactions.models.Transaction;
import org.springframework.context.ApplicationEvent;

public final class CurrencyExchangeEvent extends ApplicationEvent {

    private final Transaction transaction;

    public CurrencyExchangeEvent(Object source, Transaction transaction) {
        super(source);
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

}
