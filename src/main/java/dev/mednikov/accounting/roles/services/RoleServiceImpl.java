package dev.mednikov.accounting.roles.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.roles.dto.RoleDto;
import dev.mednikov.accounting.roles.dto.RoleDtoMapper;
import dev.mednikov.accounting.roles.exceptions.RoleNotFoundException;
import dev.mednikov.accounting.roles.models.Role;
import dev.mednikov.accounting.roles.repositories.RoleRepository;
import dev.mednikov.accounting.users.models.User;
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


}
