package dev.mednikov.accounting.roles.services;

import dev.mednikov.accounting.roles.dto.RoleDto;

import java.util.List;

public interface RoleService {

    RoleDto createRole (RoleDto payload);

    RoleDto updateRole (RoleDto payload);

    void deleteRole (Long id);

    List<RoleDto> getRoles (Long organizationId);

    void addAuthorityToRole (Long roleId, Long authorityId);

    void removeAuthorityFromRole (Long roleId, Long authorityId);

}
