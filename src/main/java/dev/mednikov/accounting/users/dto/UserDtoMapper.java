package dev.mednikov.accounting.users.dto;

import com.google.common.hash.Hashing;
import dev.mednikov.accounting.users.models.User;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public final class UserDtoMapper implements Function<User, UserDto> {

    @Override
    public UserDto apply(User user) {
        UserDto result = new UserDto();
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setId(user.getId().toString());
        result.setGravatarUrl(getGravatarUrl(user.getEmail()));
        return result;
    }

    private String getGravatarUrl(String email){
        String hash = Hashing.sha256().hashString(email, StandardCharsets.UTF_8).toString();
        String gravatarUrl = "https://www.gravatar.com/avatar/" + hash + "?d=mp";
        return gravatarUrl;
    }

}
