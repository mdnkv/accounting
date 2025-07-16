package dev.mednikov.accounting.organizations.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.organizations.dto.OrganizationUserDto;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.models.OrganizationUser;
import dev.mednikov.accounting.organizations.repositories.OrganizationUserRepository;
import dev.mednikov.accounting.roles.models.Role;
import dev.mednikov.accounting.users.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class OrganizationUserServiceImplTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
    @Mock private OrganizationUserRepository organizationUserRepository;
    @InjectMocks private OrganizationUserServiceImpl organizationUserService;

    @Test
    void getActiveForUser_existsTest(){
        Long organizationId = snowflakeGenerator.next();
        Long userId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();

        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Rapp Schramm GmbH & Co. KG");

        Role role = new Role();
        role.setId(roleId);
        role.setOrganization(organization);
        role.setName("User");

        User user = new User();
        user.setId(userId);
        user.setEmail("albrecht.horstdieter@ritter.org");
        user.setFirstName("Jutta");
        user.setLastName("Fröhlich");
        user.setKeycloakId(UUID.randomUUID().toString());

        OrganizationUser organizationUser = new OrganizationUser();
        organizationUser.setOrganization(organization);
        organizationUser.setRole(role);
        organizationUser.setUser(user);
        organizationUser.setActive(true);
        organizationUser.setId(snowflakeGenerator.next());

        Mockito.when(organizationUserRepository.findActiveForUser(userId)).thenReturn(Optional.of(organizationUser));
        Optional<OrganizationUserDto> result = organizationUserService.getActiveForUser(user);
        Assertions.assertThat(result).isPresent();
    }

    @Test
    void getActiveForUser_notExistsTest(){
        Long userId = snowflakeGenerator.next();

        User user = new User();
        user.setId(userId);
        user.setEmail("ekkehard.jakob@fiedler.com");
        user.setFirstName("Katja");
        user.setLastName("Held-Schröter");
        user.setKeycloakId(UUID.randomUUID().toString());

        Mockito.when(organizationUserRepository.findActiveForUser(userId)).thenReturn(Optional.empty());
        Optional<OrganizationUserDto> result = organizationUserService.getActiveForUser(user);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void setActiveForUser_successTest(){
        Long userId = snowflakeGenerator.next();
        Long organizationId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();
        Long organizationUserId = snowflakeGenerator.next();

        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Albrecht Böhme GmbH & Co. KGaA");

        User user = new User();
        user.setId(userId);
        user.setEmail("gudrun32@lehmann.de");
        user.setFirstName("Kunigunde");
        user.setLastName("Fleischer-Wahl");
        user.setKeycloakId(UUID.randomUUID().toString());

        Role role = new Role();
        role.setId(roleId);
        role.setOrganization(organization);
        role.setName("User");

        OrganizationUser organizationUser = new OrganizationUser();
        organizationUser.setOrganization(organization);
        organizationUser.setRole(role);
        organizationUser.setUser(user);
        organizationUser.setActive(true);
        organizationUser.setId(organizationUserId);

        Mockito.when(organizationUserRepository.findActiveForUser(userId)).thenReturn(Optional.empty());
        Mockito.when(organizationUserRepository.findById(organizationUserId)).thenReturn(Optional.of(organizationUser));
        Mockito.when(organizationUserRepository.save(organizationUser)).thenReturn(organizationUser);

        OrganizationUserDto result = organizationUserService.setActiveForUser(user, organizationUserId);
        Assertions.assertThat(result).isNotNull();
    }

}
