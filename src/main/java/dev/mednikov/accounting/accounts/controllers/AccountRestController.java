package dev.mednikov.accounting.accounts.controllers;

import dev.mednikov.accounting.accounts.dto.AccountDto;
import dev.mednikov.accounting.accounts.services.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody AccountDto createAccount(@RequestBody AccountDto accountDto) {
        return this.accountService.createAccount(accountDto);
    }

    @PutMapping("/update")
    public @ResponseBody AccountDto updateAccount(@RequestBody AccountDto accountDto) {
        return this.accountService.updateAccount(accountDto);
    }

    @DeleteMapping("/delete/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long accountId) {
        this.accountService.deleteAccount(accountId);
    }

    @GetMapping("/organization/{organizationId}")
    public @ResponseBody List<AccountDto> getAllAccounts(@PathVariable Long organizationId) {
        return this.accountService.getAccounts(organizationId);
    }

}
