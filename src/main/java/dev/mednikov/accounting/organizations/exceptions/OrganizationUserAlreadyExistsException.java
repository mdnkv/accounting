package dev.mednikov.accounting.organizations.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class OrganizationUserAlreadyExistsException extends RuntimeException{
}
