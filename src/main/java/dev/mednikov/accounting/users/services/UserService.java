package dev.mednikov.accounting.users.services;

import dev.mednikov.accounting.users.dto.CurrentUserDto;
import dev.mednikov.accounting.users.models.User;

public interface UserService {

    User getOrCreateUser (CurrentUserDto currentUserRequest);

}
