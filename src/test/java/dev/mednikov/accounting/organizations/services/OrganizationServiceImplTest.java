package dev.mednikov.accounting.organizations.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.organizations.dto.OrganizationDto;
import dev.mednikov.accounting.organizations.exceptions.OrganizationNotFoundException;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import dev.mednikov.accounting.users.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Mock private ApplicationEventPublisher eventPublisher;
    @Mock private OrganizationRepository organizationRepository;
    @InjectMocks private OrganizationServiceImpl organizationService;

    @Test
    void createOrganizationTest(){
        Long organizationId = snowflakeGenerator.next();
        OrganizationDto payload = new OrganizationDto();
        payload.setName("Wilhelm Meißner AG");

        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Wilhelm Meißner AG");

        Long userId = snowflakeGenerator.next();
        User user = new User();
        user.setId(userId);
        user.setKeycloakId(UUID.randomUUID().toString());
        user.setEmail("vy7e5804yikk6gcq33ht@ymail.com");

        Mockito.when(organizationRepository.save(organization)).thenReturn(organization);

        OrganizationDto result = organizationService.createOrganization(user, payload);
        Mockito.verify(organizationRepository, Mockito.times(1)).save(organization);
        Assertions.assertThat(result).isNotNull();

    }

    @Test
    void updateOrganization_notFoundTest(){
        Long organizationId = snowflakeGenerator.next();
        OrganizationDto payload = new OrganizationDto();
        payload.setName("Sturm Kessler GmbH");
        payload.setId(organizationId.toString());

        Mockito.when(organizationRepository.findById(organizationId)).thenReturn(Optional.empty());
        Assertions
                .assertThatThrownBy(() -> organizationService.updateOrganization(payload))
                .isInstanceOf(OrganizationNotFoundException.class);
    }

    @Test
    void updateOrganization_successTest(){
        Long organizationId = snowflakeGenerator.next();

        OrganizationDto payload = new OrganizationDto();
        payload.setName("Hanke Stiftung & Co. KG");
        payload.setId(organizationId.toString());

        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Hanke Stiftung & Co. KG");

        Mockito.when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organization));
        Mockito.when(organizationRepository.save(organization)).thenReturn(organization);

        OrganizationDto result = organizationService.updateOrganization(payload);
        Mockito.verify(organizationRepository, Mockito.times(1)).save(organization);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void getOrganizationById_existsTest(){
        Long organizationId = snowflakeGenerator.next();

        Organization organization = new Organization();
        organization.setId(organizationId);
        organization.setName("Gärtner GmbH & Co. KG");

        Mockito.when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organization));

        Optional<OrganizationDto> result = organizationService.getOrganization(organizationId);
        Assertions.assertThat(result).isPresent();
    }

    @Test
    void getOrganizationById_doesNotExistTest(){
        Long organizationId = snowflakeGenerator.next();
        Mockito.when(organizationRepository.findById(organizationId)).thenReturn(Optional.empty());
        Optional<OrganizationDto> result = organizationService.getOrganization(organizationId);
        Assertions.assertThat(result).isEmpty();
    }

}
