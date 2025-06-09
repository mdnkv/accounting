package dev.mednikov.accounting.roles.services;

import dev.mednikov.accounting.roles.dto.RoleDto;
import dev.mednikov.accounting.users.models.User;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<RoleDto> getRolesForUser (User user);

    RoleDto setActiveRole (User user, Long roleId);

    Optional<RoleDto> getActiveRole (User user);

}
