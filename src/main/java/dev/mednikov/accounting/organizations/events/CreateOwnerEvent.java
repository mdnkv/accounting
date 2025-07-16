package dev.mednikov.accounting.organizations.events;

import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.roles.models.Role;
import dev.mednikov.accounting.users.models.User;
import org.springframework.context.ApplicationEvent;

public final class CreateOwnerEvent extends ApplicationEvent {

    private final Organization organization;
    private final User user;
    private final Role role;

    public CreateOwnerEvent(Object source, Organization organization, User user, Role role) {
        super(source);
        this.organization = organization;
        this.user = user;
        this.role = role;
    }

    public Organization getOrganization() {
        return organization;
    }

    public User getUser() {
        return user;
    }

    public Role getRole() {
        return role;
    }

}
