package dev.mednikov.accounting.roles.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.roles.dto.RoleDto;
import dev.mednikov.accounting.roles.dto.RoleDtoMapper;
import dev.mednikov.accounting.roles.events.OwnerRoleEvent;
import dev.mednikov.accounting.roles.exceptions.RoleNotFoundException;
import dev.mednikov.accounting.roles.models.Role;
import dev.mednikov.accounting.roles.models.RoleType;
import dev.mednikov.accounting.roles.repositories.RoleRepository;
import dev.mednikov.accounting.users.models.User;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final static RoleDtoMapper roleDtoMapper = new RoleDtoMapper();
    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDto> getRolesForUser(User user) {
        return this.roleRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(roleDtoMapper).toList();
    }

    @Override
    public RoleDto setActiveRole(User user, Long roleId) {
        List<Role> roles = this.roleRepository.findAllByUserId(user.getId());
        List<Role> updatedRoles = roles
                .stream()
                .peek(role -> role.setActive(role.getId().equals(roleId)))
                .toList();
        this.roleRepository.saveAll(updatedRoles);
        Role activeRole = this.roleRepository.findActiveRoleForUser(user.getId()).orElseThrow(RoleNotFoundException::new);
        return roleDtoMapper.apply(activeRole);
    }

    @Override
    public Optional<RoleDto> getActiveRole(User user) {
        Long id = user.getId();
        return this.roleRepository.findActiveRoleForUser(id).map(roleDtoMapper);
    }

    @EventListener
    public void onNewOwnerRoleEventListener (OwnerRoleEvent event){
        User user = event.getOwner();
        Organization organization = event.getOrganization();

        // check that the role should be active
        boolean active = this.roleRepository.findAllByUserId(user.getId()).isEmpty();

        // Create role
        Role role = new Role();
        role.setId(snowflakeGenerator.next());
        role.setActive(active);
        role.setOrganization(organization);
        role.setUser(user);
        role.setRoleType(RoleType.OWNER);

        // Save to db
        this.roleRepository.save(role);
    }


}
