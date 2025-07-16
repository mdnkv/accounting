package dev.mednikov.accounting.organizations.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.organizations.dto.OrganizationUserDto;
import dev.mednikov.accounting.organizations.dto.OrganizationUserDtoMapper;
import dev.mednikov.accounting.organizations.events.CreateOwnerEvent;
import dev.mednikov.accounting.organizations.exceptions.OrganizationUserNotFoundException;
import dev.mednikov.accounting.organizations.models.OrganizationUser;
import dev.mednikov.accounting.organizations.repositories.OrganizationUserRepository;
import dev.mednikov.accounting.users.models.User;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationUserServiceImpl implements OrganizationUserService {

    private final static OrganizationUserDtoMapper mapper = new OrganizationUserDtoMapper();
    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    private final OrganizationUserRepository organizationUserRepository;

    public OrganizationUserServiceImpl(OrganizationUserRepository organizationUserRepository) {
        this.organizationUserRepository = organizationUserRepository;
    }

    @Override
    public Optional<OrganizationUserDto> getActiveForUser(User user) {
        return this.organizationUserRepository.findActiveForUser(user.getId()).map(mapper);
    }

    @Override
    public List<OrganizationUserDto> getAllForUser(User user) {
        return this.organizationUserRepository.findAllByUserId(user.getId()).stream().map(mapper).toList();
    }

    @Override
    public OrganizationUserDto setActiveForUser(User user, Long id) {
        // Find current active
        Optional<OrganizationUser> currentActive = this.organizationUserRepository.findActiveForUser(user.getId());
        // Set current active as not active
        if (currentActive.isPresent()) {
            OrganizationUser current = currentActive.get();
            if (current.getId().equals(id)) {
                // User just misuses API and wants a current role to be active,
                // then we just return it
                return mapper.apply(current);
            }
            current.setActive(false);
            this.organizationUserRepository.save(current);
        }
        // Find organization user by id
        OrganizationUser organizationUser = this.organizationUserRepository.findById(id).orElseThrow(OrganizationUserNotFoundException::new);
        // Set as active
        organizationUser.setActive(true);
        // Return result
        OrganizationUser result = this.organizationUserRepository.save(organizationUser);
        return mapper.apply(result);
    }

    @EventListener
    public void onCreateOwnerEventListener (CreateOwnerEvent event) {
        User user = event.getUser();
        // Check if the user has other roles
        boolean active = this.organizationUserRepository.findAllByUserId(user.getId()).isEmpty();

        // Create an organization user entity
        OrganizationUser organizationUser = new OrganizationUser();
        organizationUser.setActive(active);
        organizationUser.setUser(user);
        organizationUser.setRole(event.getRole());
        organizationUser.setOrganization(event.getOrganization());
        organizationUser.setId(snowflakeGenerator.next());
        this.organizationUserRepository.save(organizationUser);
    }

}
