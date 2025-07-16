package dev.mednikov.accounting.roles.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.authorities.dto.AuthorityDto;
import dev.mednikov.accounting.authorities.repositories.AuthorityRepository;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import dev.mednikov.accounting.roles.dto.RoleDto;
import dev.mednikov.accounting.roles.exceptions.RoleAlreadyExistsException;
import dev.mednikov.accounting.roles.exceptions.RoleNotFoundException;
import dev.mednikov.accounting.roles.models.Role;
import dev.mednikov.accounting.roles.repositories.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Mock private RoleRepository roleRepository;
    @Mock private AuthorityRepository authorityRepository;
    @Mock private OrganizationRepository organizationRepository;
    @InjectMocks private RoleServiceImpl roleService;

    @Test
    void createRole_alreadyExistsTest(){
        Long organizationId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();

        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Rupp Moser GmbH");

        Role role = new Role();
        role.setId(roleId);
        role.setName("Administrator");
        role.setOrganization(organization);

        AuthorityDto authorityDto = new AuthorityDto();
        authorityDto.setId(snowflakeGenerator.next().toString());
        authorityDto.setName("accounts:create");
        authorityDto.setOrganizationId(organizationId.toString());
        RoleDto payload = new RoleDto();
        payload.setAuthorities(List.of(authorityDto));
        payload.setOrganizationId(organizationId.toString());
        payload.setName("Administrator");

        Mockito.when(roleRepository.findByNameAndOrganizationId("Administrator", organizationId)).thenReturn(Optional.of(role));
        Assertions.assertThatThrownBy(() -> roleService.createRole(payload)).isInstanceOf(RoleAlreadyExistsException.class);
    }

    @Test
    void createRole_successTest(){
        Long organizationId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();

        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Neuhaus Schultz e.G.");

        Role role = new Role();
        role.setId(roleId);
        role.setName("Administrator");
        role.setOrganization(organization);

        RoleDto payload = new RoleDto();
        payload.setOrganizationId(organizationId.toString());
        payload.setName("Administrator");
        payload.setAuthorities(List.of());

        Mockito.when(roleRepository.findByNameAndOrganizationId("Administrator", organizationId)).thenReturn(Optional.empty());
        Mockito.when(organizationRepository.getReferenceById(organizationId)).thenReturn(organization);
        Mockito.when(roleRepository.save(role)).thenReturn(role);

        RoleDto result = roleService.createRole(payload);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void updateRole_alreadyExistsTest(){
        Long organizationId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();

        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Fuhrmann e.V.");

        Role role = new Role();
        role.setId(roleId);
        role.setName("Administrator");
        role.setOrganization(organization);

        RoleDto payload = new RoleDto();
        payload.setId(roleId.toString());
        payload.setOrganizationId(organizationId.toString());
        payload.setName("User");
        payload.setAuthorities(List.of());

        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        Mockito.when(roleRepository.findByNameAndOrganizationId("User", organizationId)).thenReturn(Optional.of(new Role()));
        Assertions.assertThatThrownBy(() -> roleService.updateRole(payload)).isInstanceOf(RoleAlreadyExistsException.class);
    }

    @Test
    void updateRole_notFoundTest(){
        Long organizationId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();

        RoleDto payload = new RoleDto();
        payload.setId(roleId.toString());
        payload.setOrganizationId(organizationId.toString());
        payload.setName("User");
        payload.setAuthorities(List.of());

        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> roleService.updateRole(payload)).isInstanceOf(RoleNotFoundException.class);
    }

    @Test
    void updateRole_successTest(){
        Long organizationId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();

        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Bode Hauser AG");

        Role role = new Role();
        role.setId(roleId);
        role.setName("Administrator");
        role.setOrganization(organization);

        RoleDto payload = new RoleDto();
        payload.setId(roleId.toString());
        payload.setOrganizationId(organizationId.toString());
        payload.setName("User");
        payload.setAuthorities(List.of());

        Mockito.when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        Mockito.when(roleRepository.findByNameAndOrganizationId("User", organizationId)).thenReturn(Optional.empty());
        Mockito.when(roleRepository.save(role)).thenReturn(role);
        RoleDto result = roleService.updateRole(payload);
        Assertions.assertThat(result).isNotNull();
    }

}
