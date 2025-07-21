package dev.mednikov.accounting.organizations.dto;

import dev.mednikov.accounting.roles.dto.RoleDto;

public final class UserOrganizationDto {

    private String id;
    private OrganizationDto organization;
    private RoleDto role;
    private boolean active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrganizationDto getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDto organization) {
        this.organization = organization;
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
