package dev.mednikov.accounting.users.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.users.events.UserCreatedEvent;
import dev.mednikov.accounting.users.models.User;
import dev.mednikov.accounting.users.repositories.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
    public User getOrCreateUser(Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        Optional<User> result = this.userRepository.findByKeycloakId(keycloakId);
        String email = principal.getClaimAsString("email");
        String firstName = principal.getClaimAsString("given_name");
        String lastName = principal.getClaimAsString("family_name");

        if (result.isPresent()) {
            // Check that user was updated
            boolean updated = false;
            User user = result.get();
            if (!user.getFirstName().equals(firstName)){
                user.setFirstName(firstName);
                updated = true;
            }
            if (!user.getLastName().equals(lastName)){
                user.setLastName(lastName);
                updated = true;
            }
            if (!user.getEmail().equals(email)){
                user.setEmail(email);
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
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setKeycloakId(keycloakId);
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
