package dev.mednikov.accounting.currencies.controllers;

import dev.mednikov.accounting.currencies.dto.CurrencyDto;
import dev.mednikov.accounting.currencies.services.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyRestController {

    private final CurrencyService currencyService;

    public CurrencyRestController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('currencies:create')")
    public @ResponseBody CurrencyDto createCurrency(@RequestBody CurrencyDto body){
        return this.currencyService.createCurrency(body);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('currencies:update')")
    public @ResponseBody CurrencyDto updateCurrency(@RequestBody CurrencyDto body){
        return this.currencyService.updateCurrency(body);
    }

    @GetMapping("/organization/{organizationId}")
    @PreAuthorize("hasAuthority('currencies:view')")
    public @ResponseBody List<CurrencyDto> getCurrencies(@PathVariable Long organizationId){
        return this.currencyService.getCurrencies(organizationId);
    }

    @GetMapping("/primary/{organizationId}")
    @PreAuthorize("hasAuthority('currencies:view')")
    public ResponseEntity<CurrencyDto> getPrimaryCurrency(@PathVariable Long organizationId){
        Optional<CurrencyDto> result = this.currencyService.getPrimaryCurrency(organizationId);
        return ResponseEntity.of(result);
    }

}
