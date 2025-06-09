package dev.mednikov.accounting.roles.repositories;

import dev.mednikov.accounting.roles.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findAllByUserId(Long userId);

    Optional<Role> findByOrganizationIdAndUserId (Long organizationId, Long userId);

    @Query("SELECT r FROM Role r WHERE r.user.id = :userId AND r.active = true")
    Optional<Role> findActiveRoleForUser (Long userId);

}
