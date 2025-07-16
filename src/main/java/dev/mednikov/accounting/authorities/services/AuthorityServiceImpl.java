package dev.mednikov.accounting.authorities.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.authorities.dto.AuthorityDto;
import dev.mednikov.accounting.authorities.dto.AuthorityDtoMapper;
import dev.mednikov.accounting.authorities.exceptions.AuthorityAlreadyExistsException;
import dev.mednikov.accounting.authorities.exceptions.AuthorityNotFoundException;
import dev.mednikov.accounting.authorities.models.Authority;
import dev.mednikov.accounting.authorities.repositories.AuthorityRepository;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import dev.mednikov.accounting.roles.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
    private final static AuthorityDtoMapper mapper = new AuthorityDtoMapper();

    private final AuthorityRepository authorityRepository;
    private final OrganizationRepository organizationRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository, OrganizationRepository organizationRepository) {
        this.authorityRepository = authorityRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public AuthorityDto createAuthority(AuthorityDto payload) {
        Long organizationId = Long.valueOf(payload.getOrganizationId());
        String name = payload.getName();
        if (this.authorityRepository.findByOrganizationIdAndName(organizationId, name).isPresent()) {
            throw new AuthorityAlreadyExistsException();
        }

        Organization organization = this.organizationRepository.getReferenceById(organizationId);
        Authority authority = new Authority();
        authority.setOrganization(organization);
        authority.setName(name);
        authority.setId(snowflakeGenerator.next());

        Authority result = this.authorityRepository.save(authority);

        return mapper.apply(result);
    }

    @Override
    public AuthorityDto updateAuthority(AuthorityDto payload) {
        Objects.requireNonNull(payload.getId());
        Long id = Long.valueOf(payload.getId());
        Authority authority = this.authorityRepository.findById(id).orElseThrow(AuthorityNotFoundException::new);

        if (!authority.getName().equals(payload.getName())) {
            Long organizationId = Long.valueOf(payload.getOrganizationId());
            if (this.authorityRepository.findByOrganizationIdAndName(organizationId, payload.getName()).isPresent()){
                throw new AuthorityAlreadyExistsException();
            }
        }

        authority.setName(payload.getName());
        Authority result = this.authorityRepository.save(authority);

        return mapper.apply(result);
    }

    @Override
    public void deleteAuthority(Long id) {
        this.authorityRepository.deleteById(id);

    }

    @Override
    public List<AuthorityDto> getAuthorities(Long organizationId) {
        return this.authorityRepository.findByOrganizationId(organizationId)
                .stream()
                .map(mapper)
                .toList();
    }

}
