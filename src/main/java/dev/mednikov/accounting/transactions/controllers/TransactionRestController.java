package dev.mednikov.accounting.transactions.controllers;

import dev.mednikov.accounting.transactions.dto.TransactionDto;
import dev.mednikov.accounting.transactions.services.TransactionService;
import dev.mednikov.accounting.users.models.User;
import dev.mednikov.accounting.users.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionRestController {

    private final UserService userService;
    private final TransactionService transactionService;

    public TransactionRestController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody TransactionDto createTransaction(@RequestBody TransactionDto transactionDto, @AuthenticationPrincipal Jwt jwt) {
        User user = this.userService.getOrCreateUser(jwt);
        return this.transactionService.createTransaction(user, transactionDto);
    }

    @GetMapping("/organization/{organizationId}")
    public @ResponseBody List<TransactionDto> getAllTransactions(@PathVariable Long organizationId) {
        return this.transactionService.getTransactions(organizationId);
    }

}
