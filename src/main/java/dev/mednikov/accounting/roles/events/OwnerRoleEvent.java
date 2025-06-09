package dev.mednikov.accounting.roles.events;

import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.users.models.User;
import org.springframework.context.ApplicationEvent;

public final class OwnerRoleEvent extends ApplicationEvent {

    private final User owner;
    private final Organization organization;

    public OwnerRoleEvent(Object source, User owner, Organization organization) {
        super(source);
        this.owner = owner;
        this.organization = organization;
    }

    public User getOwner() {
        return owner;
    }

    public Organization getOrganization() {
        return organization;
    }
}
