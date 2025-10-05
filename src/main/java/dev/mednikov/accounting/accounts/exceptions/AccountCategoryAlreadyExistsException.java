package dev.mednikov.accounting.accounts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public final class AccountCategoryAlreadyExistsException extends RuntimeException {
}
