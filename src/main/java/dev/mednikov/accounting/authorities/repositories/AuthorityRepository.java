package dev.mednikov.accounting.authorities.repositories;

import dev.mednikov.accounting.authorities.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findByOrganizationIdAndName(Long organizationId, String name);

    List<Authority> findByOrganizationId(Long organizationId);

}
