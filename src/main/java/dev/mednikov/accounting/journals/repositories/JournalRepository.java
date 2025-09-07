package dev.mednikov.accounting.journals.repositories;

import dev.mednikov.accounting.journals.models.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {

    Optional<Journal> findByOrganizationIdAndName(Long organizationId, String name);

    List<Journal> findAllByOrganizationId(Long organizationId);

}
