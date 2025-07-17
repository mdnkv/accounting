package dev.mednikov.accounting.transactions.controllers;

import dev.mednikov.accounting.transactions.dto.TransactionDto;
import dev.mednikov.accounting.transactions.services.TransactionService;
import dev.mednikov.accounting.users.models.User;
import dev.mednikov.accounting.users.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    @PreAuthorize("hasAuthority('transactions:create')")
    public @ResponseBody TransactionDto createTransaction(@RequestBody TransactionDto transactionDto, @AuthenticationPrincipal Jwt jwt) {
        return this.transactionService.createTransaction(transactionDto);
    }

    @GetMapping("/organization/{organizationId}")
    @PreAuthorize("hasAuthority('transactions:view')")
    public @ResponseBody List<TransactionDto> getAllTransactions(@PathVariable Long organizationId) {
        return this.transactionService.getTransactions(organizationId);
    }

    @DeleteMapping("/delete/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('transactions:delete')")
    public void deleteTransaction(@PathVariable Long transactionId) {
        this.transactionService.deleteTransaction(transactionId);
    }

}
