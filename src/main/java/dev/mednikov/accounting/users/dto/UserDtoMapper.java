package dev.mednikov.accounting.users.dto;

import dev.mednikov.accounting.users.models.User;

import java.util.function.Function;

public final class UserDtoMapper implements Function<User, UserDto> {

    @Override
    public UserDto apply(User user) {
        UserDto result = new UserDto();
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setId(user.getId().toString());
        return result;
    }

}
