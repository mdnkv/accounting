package dev.mednikov.accounting.currencies.services;

import dev.mednikov.accounting.currencies.clients.CurrencyExchangeRestClient;
import dev.mednikov.accounting.currencies.events.CurrencyExchangeEvent;
import dev.mednikov.accounting.currencies.models.Currency;
import dev.mednikov.accounting.transactions.models.Transaction;
import dev.mednikov.accounting.transactions.models.TransactionLine;
import dev.mednikov.accounting.transactions.repositories.TransactionLineRepository;
import dev.mednikov.accounting.transactions.repositories.TransactionRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyExchangeService {

    private final CurrencyExchangeRestClient exchangeRestClient;
    private final TransactionRepository transactionRepository;
    private final TransactionLineRepository transactionLineRepository;

    public CurrencyExchangeService(TransactionRepository transactionRepository, TransactionLineRepository transactionLineRepository, CurrencyExchangeRestClient exchangeRestClient) {
        this.transactionRepository = transactionRepository;
        this.transactionLineRepository = transactionLineRepository;
        this.exchangeRestClient = exchangeRestClient;
    }

    @Async
    @EventListener
    public void onCurrencyExchangeEventListener (CurrencyExchangeEvent event){
        // Get base and target currency
        Transaction transaction = event.getTransaction();
        Currency primaryCurrency = transaction.getBaseCurrency();
        Currency transactionCurrency = transaction.getTargetCurrency();

        // Get exchange rate
        BigDecimal exchangeRate = this.exchangeRestClient.getExchangeRate(transaction.getDate(), transactionCurrency, primaryCurrency);

        // Adjust transaction lines
        for (TransactionLine transactionLine : transaction.getLines()) {
            BigDecimal debit = transactionLine.getOriginalDebitAmount().multiply(exchangeRate);
            BigDecimal credit = transactionLine.getOriginalCreditAmount().multiply(exchangeRate);
            transactionLine.setDebitAmount(debit);
            transactionLine.setCreditAmount(credit);
        }

        // Adjust transaction amounts in primary currency
        BigDecimal exchangedDebitAmount = transaction.getOriginalTotalDebitAmount().multiply(exchangeRate);
        BigDecimal exchangedCreditAmount = transaction.getOriginalTotalCreditAmount().multiply(exchangeRate);
        transaction.setTotalCreditAmount(exchangedCreditAmount);
        transaction.setTotalDebitAmount(exchangedDebitAmount);
        transaction.setDraft(false);

        this.transactionLineRepository.saveAll(transaction.getLines());
        this.transactionRepository.save(transaction);
    }
}
