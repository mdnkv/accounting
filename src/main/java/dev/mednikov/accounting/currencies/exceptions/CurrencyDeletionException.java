package dev.mednikov.accounting.currencies.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CurrencyDeletionException extends RuntimeException{
}
