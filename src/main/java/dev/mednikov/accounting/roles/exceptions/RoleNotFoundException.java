package dev.mednikov.accounting.roles.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class RoleNotFoundException extends RuntimeException{
}
