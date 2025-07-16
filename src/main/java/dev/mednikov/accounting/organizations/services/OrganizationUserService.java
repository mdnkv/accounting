package dev.mednikov.accounting.organizations.services;

import dev.mednikov.accounting.organizations.dto.OrganizationUserDto;
import dev.mednikov.accounting.users.models.User;

import java.util.List;
import java.util.Optional;

public interface OrganizationUserService {

    Optional<OrganizationUserDto> getActiveForUser (User user);

    List<OrganizationUserDto> getAllForUser (User user);

    OrganizationUserDto setActiveForUser (User user, Long id);

}
