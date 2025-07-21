package dev.mednikov.accounting.organizations.repositories;

import dev.mednikov.accounting.organizations.models.OrganizationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationUserRepository extends JpaRepository<OrganizationUser, Long> {

    @Query("SELECT ou FROM OrganizationUser ou WHERE ou.user.id = :userId AND ou.active=true")
    Optional<OrganizationUser> findActiveForUser (Long userId);

    Optional<OrganizationUser> findByOrganizationIdAndUserId(Long organizationId, Long userId);

    List<OrganizationUser> findAllByUserId(Long userId);

    List<OrganizationUser> findAllByOrganizationId(Long organizationId);

}
