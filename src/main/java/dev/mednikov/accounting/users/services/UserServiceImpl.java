package dev.mednikov.accounting.users.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.users.dto.CurrentUserDto;
import dev.mednikov.accounting.users.events.UserCreatedEvent;
import dev.mednikov.accounting.users.models.User;
import dev.mednikov.accounting.users.repositories.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public User getOrCreateUser(CurrentUserDto currentUserRequest) {
        Optional<User> result = this.userRepository.findByKeycloakId(currentUserRequest.getAuthId());

        if (result.isPresent()) {
            // Check that user was updated
            boolean updated = false;
            User user = result.get();
            if (!user.getFirstName().equals(currentUserRequest.getFirstName())){
                user.setFirstName(currentUserRequest.getFirstName());
                updated = true;
            }
            if (!user.getLastName().equals(currentUserRequest.getLastName())){
                user.setLastName(currentUserRequest.getLastName());
                updated = true;
            }
            if (!user.getEmail().equals(currentUserRequest.getEmail())){
                user.setEmail(currentUserRequest.getEmail());
                updated = true;
            }

            // Save user to db if was changed
            if (updated){
                return this.userRepository.save(user);
            } else {
                return user;
            }
        } else {
            // Create user
            User user = new User();
            user.setEmail(currentUserRequest.getEmail());
            user.setFirstName(currentUserRequest.getFirstName());
            user.setLastName(currentUserRequest.getLastName());
            user.setKeycloakId(currentUserRequest.getAuthId());
            user.setId(snowflakeGenerator.next());
            user.setActive(true);
            user.setSuperuser(false);

            User savedUser = this.userRepository.save(user);

            // Also check for invitations
            UserCreatedEvent event = new UserCreatedEvent(this, savedUser);
            this.eventPublisher.publishEvent(event);

            return savedUser;
        }
    }

}
