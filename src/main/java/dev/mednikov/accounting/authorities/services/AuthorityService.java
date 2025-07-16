package dev.mednikov.accounting.authorities.services;

import dev.mednikov.accounting.authorities.dto.AuthorityDto;

import java.util.List;

public interface AuthorityService {

    AuthorityDto createAuthority(AuthorityDto payload);

    AuthorityDto updateAuthority(AuthorityDto payload);

    void deleteAuthority (Long id);

    List<AuthorityDto> getAuthorities(Long organizationId);

}
