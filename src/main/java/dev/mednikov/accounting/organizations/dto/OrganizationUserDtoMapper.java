package dev.mednikov.accounting.organizations.dto;

import dev.mednikov.accounting.organizations.models.OrganizationUser;
import dev.mednikov.accounting.roles.dto.RoleDto;
import dev.mednikov.accounting.roles.dto.RoleDtoMapper;

import java.util.function.Function;

public final class OrganizationUserDtoMapper implements Function<OrganizationUser, OrganizationUserDto> {

    private final static RoleDtoMapper roleDtoMapper = new RoleDtoMapper();
    private final static OrganizationDtoMapper organizationDtoMapper = new OrganizationDtoMapper();

    @Override
    public OrganizationUserDto apply(OrganizationUser organizationUser) {
        OrganizationDto organization = organizationDtoMapper.apply(organizationUser.getOrganization());
        RoleDto role = roleDtoMapper.apply(organizationUser.getRole());
        OrganizationUserDto result = new OrganizationUserDto();
        result.setOrganization(organization);
        result.setRole(role);
        result.setId(organizationUser.getId().toString());
        result.setActive(organizationUser.isActive());
        return result;
    }

}
