package dev.mednikov.accounting.roles.repositories;

import dev.mednikov.accounting.roles.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNameAndOrganizationId (String name, Long organizationId);

    List<Role> findByOrganizationId(Long organizationId);

}
