package dev.mednikov.accounting.transactions.controllers;

import dev.mednikov.accounting.transactions.dto.TransactionDto;
import dev.mednikov.accounting.transactions.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRestController {

    private final TransactionService transactionService;

    public TransactionRestController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody TransactionDto createTransaction(@RequestBody TransactionDto transactionDto) {
        return this.transactionService.createTransaction(transactionDto);
    }

    @GetMapping("/organization/{organizationId}")
    public @ResponseBody List<TransactionDto> getAllTransactions(@PathVariable Long organizationId) {
        return this.transactionService.getTransactions(organizationId);
    }

}
