package dev.mednikov.accounting.users.dto;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;
import java.util.function.Function;

public class CurrentUserDtoMapper implements Function<Jwt, CurrentUserDto> {

    @Override
    public CurrentUserDto apply(Jwt jwt) {
        CurrentUserDto currentUserDto = new CurrentUserDto();
        currentUserDto.setEmail(jwt.getClaimAsString("email"));
        currentUserDto.setFirstName(jwt.getClaimAsString("given_name"));
        currentUserDto.setLastName(jwt.getClaimAsString("family_name"));
        currentUserDto.setAuthId(UUID.fromString(jwt.getSubject()));
        return currentUserDto;
    }
}
