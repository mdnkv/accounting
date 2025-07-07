package dev.mednikov.accounting.roles.dto;

import dev.mednikov.accounting.organizations.dto.OrganizationDto;
import dev.mednikov.accounting.organizations.dto.OrganizationDtoMapper;
import dev.mednikov.accounting.roles.models.Role;

import java.util.function.Function;

public final class RoleDtoMapper implements Function<Role, RoleDto> {

    private final static OrganizationDtoMapper organizationDtoMapper = new OrganizationDtoMapper();

    @Override
    public RoleDto apply(Role role) {
        OrganizationDto organizationDto = organizationDtoMapper.apply(role.getOrganization());
        RoleDto result = new RoleDto();
        result.setId(role.getId().toString());
        result.setOrganization(organizationDto);
        result.setActive(role.isActive());
        result.setRoleType(role.getRoleType());
        return result;
    }

}
