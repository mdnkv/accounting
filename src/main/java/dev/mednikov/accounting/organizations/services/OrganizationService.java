package dev.mednikov.accounting.organizations.services;

import dev.mednikov.accounting.organizations.dto.OrganizationDto;

import java.util.Optional;

public interface OrganizationService {

    OrganizationDto createOrganization(OrganizationDto organizationDto);

    OrganizationDto updateOrganization(OrganizationDto organizationDto);

    void deleteOrganization(Long id);

    Optional<OrganizationDto> getOrganization(Long id);

}
