package dev.mednikov.accounting.accounts.repositories;

import dev.mednikov.accounting.accounts.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByOrganizationIdAndCode (Long organizationId, String code);

    @Query("SELECT ac FROM Account ac WHERE ac.organization.id = :organizationId ORDER BY ac.code")
    List<Account> findAllByOrganizationId(Long organizationId);

    @Query("SELECT ac FROM Account ac WHERE ac.organization.id = :organizationId AND ac.deprecated = false ORDER BY ac.code")
    List<Account> findActiveByOrganizationId(Long organizationId);

}
