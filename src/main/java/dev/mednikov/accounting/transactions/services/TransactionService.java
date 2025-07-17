package dev.mednikov.accounting.transactions.services;

import dev.mednikov.accounting.transactions.dto.TransactionDto;
import dev.mednikov.accounting.users.models.User;

import java.util.List;

public interface TransactionService {

    TransactionDto createTransaction(TransactionDto transactionDto);

    List<TransactionDto> getTransactions (Long organizationId);

    void deleteTransaction(Long transactionId);

}
