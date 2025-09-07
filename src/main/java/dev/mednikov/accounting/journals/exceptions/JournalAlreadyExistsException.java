package dev.mednikov.accounting.journals.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public final class JournalAlreadyExistsException extends RuntimeException {

}
