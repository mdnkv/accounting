package dev.mednikov.accounting.roles.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.accounting.authorities.exceptions.AuthorityNotFoundException;
import dev.mednikov.accounting.authorities.models.Authority;
import dev.mednikov.accounting.authorities.repositories.AuthorityRepository;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import dev.mednikov.accounting.roles.dto.RoleDto;
import dev.mednikov.accounting.roles.dto.RoleDtoMapper;
import dev.mednikov.accounting.roles.exceptions.RoleAlreadyExistsException;
import dev.mednikov.accounting.roles.exceptions.RoleNotFoundException;
import dev.mednikov.accounting.roles.models.Role;
import dev.mednikov.accounting.roles.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class RoleServiceImpl implements RoleService {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
    private final static RoleDtoMapper mapper = new RoleDtoMapper();

    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;
    private final AuthorityRepository authorityRepository;

    public RoleServiceImpl(RoleRepository roleRepository, OrganizationRepository organizationRepository, AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.organizationRepository = organizationRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public RoleDto createRole(RoleDto payload) {
        Long organizationId = Long.valueOf(payload.getOrganizationId());
        String name = payload.getName();

        if (this.roleRepository.findByNameAndOrganizationId(name, organizationId).isPresent()) {
            throw new RoleAlreadyExistsException();
        }

        Organization organization = this.organizationRepository.getReferenceById(organizationId);
        Role role = new Role();
        role.setName(name);
        role.setId(snowflakeGenerator.next());
        role.setOrganization(organization);

        // Set authorities
        if (!payload.getAuthorities().isEmpty()){
            List<Long> authoritiesIds = payload.getAuthorities()
                    .stream()
                    .map(dto -> Long.parseLong(dto.getId()))
                    .toList();
            List<Authority> authorities = this.authorityRepository.findAllById(authoritiesIds);
            role.setAuthorities(new HashSet<>(authorities));
        }

        Role result = this.roleRepository.save(role);
        return mapper.apply(result);
    }

    @Override
    public RoleDto updateRole(RoleDto payload) {
        Objects.requireNonNull(payload.getId());
        Long roleId = Long.valueOf(payload.getId());

        Role role = this.roleRepository.findById(roleId).orElseThrow(RoleNotFoundException::new);
        if (!role.getName().equals(payload.getName())) {
            // name is changed, need to verify that no other role is present
            Long organizationId = Long.valueOf(payload.getOrganizationId());
            if (this.roleRepository.findByNameAndOrganizationId(payload.getName(), organizationId).isPresent()) {
                throw new RoleAlreadyExistsException();
            }
        }

        role.setName(payload.getName());
        Role result = this.roleRepository.save(role);

        return mapper.apply(result);
    }

    @Override
    public void deleteRole(Long id) {
        this.roleRepository.deleteById(id);
    }

    @Override
    public List<RoleDto> getRoles(Long organizationId) {
        return this.roleRepository.findByOrganizationId(organizationId)
                .stream()
                .map(mapper)
                .toList();
    }

    @Override
    public void addAuthorityToRole(Long roleId, Long authorityId) {
        Role role = this.roleRepository.findById(roleId).orElseThrow(RoleNotFoundException::new);
        Authority authority = this.authorityRepository.findById(authorityId).orElseThrow(AuthorityNotFoundException::new);
        boolean result = role.getAuthorities().add(authority);
        if (result){
            this.roleRepository.save(role);
            this.authorityRepository.save(authority);
        }
    }

    @Override
    public void removeAuthorityFromRole(Long roleId, Long authorityId) {
        Role role = this.roleRepository.findById(roleId).orElseThrow(RoleNotFoundException::new);
        Authority authority = this.authorityRepository.findById(authorityId).orElseThrow(AuthorityNotFoundException::new);
        boolean deleted = role.getAuthorities().remove(authority);
        if (deleted){
            this.roleRepository.save(role);
            this.authorityRepository.save(authority);
        }
    }
}
