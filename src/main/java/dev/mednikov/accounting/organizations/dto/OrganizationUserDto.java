package dev.mednikov.accounting.organizations.dto;

import dev.mednikov.accounting.roles.dto.RoleDto;
import dev.mednikov.accounting.users.dto.UserDto;

public final class OrganizationUserDto {

    private String id;
    private String organizationId;
    private String roleId;
    private RoleDto role;
    private UserDto user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
