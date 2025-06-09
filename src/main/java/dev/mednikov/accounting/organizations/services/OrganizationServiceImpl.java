package dev.mednikov.accounting.organizations.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.organizations.dto.OrganizationDto;
import dev.mednikov.accounting.organizations.dto.OrganizationDtoMapper;
import dev.mednikov.accounting.organizations.events.OrganizationCreatedEvent;
import dev.mednikov.accounting.organizations.exceptions.OrganizationNotFoundException;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import dev.mednikov.accounting.roles.events.OwnerRoleEvent;
import dev.mednikov.accounting.users.models.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
    private final static OrganizationDtoMapper organizationDtoMapper = new OrganizationDtoMapper();

    private final ApplicationEventPublisher eventPublisher;
    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, ApplicationEventPublisher eventPublisher) {
        this.organizationRepository = organizationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public OrganizationDto createOrganization(User owner, OrganizationDto organizationDto) {
        Organization organization = new Organization();
        organization.setName(organizationDto.getName());
        organization.setCurrency(organizationDto.getCurrency());
        organization.setId(snowflakeGenerator.next());

        Organization result = this.organizationRepository.save(organization);

        // Send events
        OrganizationCreatedEvent oce = new OrganizationCreatedEvent(this, result);
        eventPublisher.publishEvent(oce);

        OwnerRoleEvent ore = new OwnerRoleEvent(this, owner, result);
        eventPublisher.publishEvent(ore);

        return organizationDtoMapper.apply(result);
    }

    @Override
    public OrganizationDto updateOrganization(OrganizationDto organizationDto) {
        Objects.requireNonNull(organizationDto.getId());
        Long organizationId = Long.valueOf(organizationDto.getId());
        Organization organization = this.organizationRepository.findById(organizationId).orElseThrow(OrganizationNotFoundException::new);
        organization.setName(organizationDto.getName());
        organization.setCurrency(organizationDto.getCurrency());
        Organization result = this.organizationRepository.save(organization);
        return organizationDtoMapper.apply(result);
    }

    @Override
    public void deleteOrganization(Long id) {
        this.organizationRepository.deleteById(id);
    }

    @Override
    public Optional<OrganizationDto> getOrganization(Long id) {
        return this.organizationRepository.findById(id).map(organizationDtoMapper);
    }

}
