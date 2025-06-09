package dev.mednikov.accounting.organizations.events;

import dev.mednikov.accounting.organizations.models.Organization;
import org.springframework.context.ApplicationEvent;

public final class OrganizationCreatedEvent extends ApplicationEvent {

    private final Organization organization;

    public OrganizationCreatedEvent(Object source, Organization organization) {
        super(source);
        this.organization = organization;
    }

    public Organization getOrganization() {
        return organization;
    }
}
