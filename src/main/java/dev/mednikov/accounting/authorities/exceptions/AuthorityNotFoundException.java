package dev.mednikov.accounting.authorities.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class AuthorityNotFoundException extends RuntimeException {
}
