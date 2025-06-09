package dev.mednikov.accounting.users.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.users.models.User;
import dev.mednikov.accounting.users.repositories.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getOrCreateUser(Jwt principal) {
        String keycloakId = principal.getSubject();
        Optional<User> result = this.userRepository.findByKeycloakId(keycloakId);
        if (result.isPresent()) {
            // Return user
            return result.get();
        } else {
            // Create user
            String email = principal.getClaimAsString("email");
            String firstName = principal.getClaimAsString("given_name");
            String lastName = principal.getClaimAsString("family_name");

            User user = new User();
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setKeycloakId(keycloakId);
            user.setId(snowflakeGenerator.next());

            return this.userRepository.save(user);
        }
    }

}
