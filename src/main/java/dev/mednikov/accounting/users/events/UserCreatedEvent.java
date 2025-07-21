package dev.mednikov.accounting.users.events;

import dev.mednikov.accounting.users.models.User;
import org.springframework.context.ApplicationEvent;

public final class UserCreatedEvent extends ApplicationEvent {

    private final User user;

    public UserCreatedEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
