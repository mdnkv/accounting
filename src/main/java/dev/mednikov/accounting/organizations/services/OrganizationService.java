package dev.mednikov.accounting.organizations.services;

import dev.mednikov.accounting.organizations.dto.OrganizationDto;
import dev.mednikov.accounting.users.models.User;

import java.util.Optional;

public interface OrganizationService {

    OrganizationDto createOrganization(User owner, OrganizationDto organizationDto);

    OrganizationDto updateOrganization(OrganizationDto organizationDto);

    void deleteOrganization(Long id);

    Optional<OrganizationDto> getOrganization(Long id);

}
