package dev.mednikov.accounting.accounts.repositories;

import dev.mednikov.accounting.accounts.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByOrganizationIdAndCode (Long organizationId, String code);

    List<Account> findByOrganizationId(Long organizationId);

}
