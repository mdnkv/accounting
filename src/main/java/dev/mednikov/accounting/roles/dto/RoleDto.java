package dev.mednikov.accounting.roles.dto;

import dev.mednikov.accounting.organizations.dto.OrganizationDto;
import dev.mednikov.accounting.roles.models.RoleType;

public final class RoleDto {

    private String id;
    private OrganizationDto organization;
    private RoleType roleType;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
