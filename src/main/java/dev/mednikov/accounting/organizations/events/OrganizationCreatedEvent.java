package dev.mednikov.accounting.organizations.events;

import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.users.models.User;
import org.springframework.context.ApplicationEvent;

public final class OrganizationCreatedEvent extends ApplicationEvent {

    private final Organization organization;
    private final User owner;

    public OrganizationCreatedEvent(Object source, Organization organization, User owner) {
        super(source);
        this.organization = organization;
        this.owner = owner;
    }

    public Organization getOrganization() {
        return organization;
    }

    public User getOwner() {
        return owner;
    }

}
