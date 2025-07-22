package dev.mednikov.accounting.organizations.services;

import dev.mednikov.accounting.organizations.dto.CreateOrganizationUserRequestDto;
import dev.mednikov.accounting.organizations.dto.OrganizationUserDto;
import dev.mednikov.accounting.organizations.dto.UserOrganizationDto;
import dev.mednikov.accounting.users.models.User;

import java.util.List;
import java.util.Optional;

public interface OrganizationUserService {

    Optional<OrganizationUserDto> createOrganizationUser (CreateOrganizationUserRequestDto payload);

    OrganizationUserDto updateOrganizationUser (OrganizationUserDto payload);

    List<OrganizationUserDto> getUsersInOrganization (Long organizationId);

    Optional<UserOrganizationDto> getActiveForUser (User user);

    List<UserOrganizationDto> getAllForUser (User user);

    UserOrganizationDto setActiveForUser (User user, Long id);

    void deleteOrganizationUser (Long id);

}
