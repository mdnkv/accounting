package dev.mednikov.accounting.organizations.repositories;

import dev.mednikov.accounting.organizations.models.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findByEmailAndOrganizationId(String email, Long organizationId);

    List<Invitation> findAllByEmail(String email);

}
