package dev.mednikov.accounting.roles.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.roles.dto.RoleDto;
import dev.mednikov.accounting.roles.models.Role;
import dev.mednikov.accounting.roles.models.RoleType;
import dev.mednikov.accounting.roles.repositories.RoleRepository;
import dev.mednikov.accounting.users.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Mock private RoleRepository roleRepository;
    @InjectMocks private RoleServiceImpl roleService;

    @Test
    void getRolesForUserTest(){
        User user = new User();
        Long userId = snowflakeGenerator.next();
        user.setId(userId);
        user.setKeycloakId(UUID.randomUUID().toString());
        user.setEmail("s5w2qovnis9gwekmho@ymail.com");

        Organization organization = new Organization();
        organization.setId(snowflakeGenerator.next());
        organization.setName("Pfeiffer Gebhardt GmbH & Co. KGaA");

        Role role = new Role();
        role.setId(snowflakeGenerator.next());
        role.setOrganization(organization);
        role.setUser(user);
        role.setRoleType(RoleType.ADMINISTRATOR);
        role.setActive(true);

        Mockito.when(roleRepository.findAllByUserId(userId)).thenReturn(List.of(role));
        List<RoleDto> roles = roleService.getRolesForUser(user);
        Assertions.assertThat(roles.size()).isEqualTo(1);
    }

    @Test
    void getActiveRoleForUser_existsTest(){
        User user = new User();
        Long userId = snowflakeGenerator.next();
        user.setId(userId);
        user.setKeycloakId(UUID.randomUUID().toString());
        user.setEmail("3ekomf00wwlyyuszyzpw@ymail.com");

        Organization organization = new Organization();
        organization.setId(snowflakeGenerator.next());
        organization.setName("Conrad Dittrich Stiftung & Co. KGaA");

        Role role = new Role();
        role.setId(snowflakeGenerator.next());
        role.setOrganization(organization);
        role.setUser(user);
        role.setRoleType(RoleType.ADMINISTRATOR);
        role.setActive(true);

        Mockito.when(roleRepository.findActiveRoleForUser(userId)).thenReturn(Optional.of(role));
        Optional<RoleDto> roleDto = roleService.getActiveRole(user);
        Assertions.assertThat(roleDto).isPresent();
    }

    @Test
    void getActiveRoleForUser_notExistsTest(){
        Long userId = snowflakeGenerator.next();
        User user = new User();
        user.setId(userId);
        user.setKeycloakId(UUID.randomUUID().toString());
        user.setEmail("4gba3b4oxegzl@googlemail.com");
        Mockito.when(roleRepository.findActiveRoleForUser(userId)).thenReturn(Optional.empty());
        Optional<RoleDto> roleDto = roleService.getActiveRole(user);
        Assertions.assertThat(roleDto).isEmpty();
    }

    @Test
    void setActiveRoleForUserTest(){
        Long userId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();

        User user = new User();
        user.setId(userId);
        user.setKeycloakId(UUID.randomUUID().toString());
        user.setEmail("4gba3b4oxegzl@ymail.com");

        Organization organization = new Organization();
        organization.setId(snowflakeGenerator.next());
        organization.setName("Raab GmbH & Co. OHG");

        Role role = new Role();
        role.setId(roleId);
        role.setOrganization(organization);
        role.setUser(user);
        role.setRoleType(RoleType.ADMINISTRATOR);
        role.setActive(true);

        Mockito.when(roleRepository.findAllByUserId(userId)).thenReturn(List.of(role));
        Mockito.when(roleRepository.saveAll(Mockito.any())).thenReturn(List.of(role));
        Mockito.when(roleRepository.findActiveRoleForUser(userId)).thenReturn(Optional.of(role));

        RoleDto result = roleService.setActiveRole(user, roleId);
        Assertions.assertThat(result).isNotNull();
    }

}
