package dev.mednikov.accounting.accounts.repositories;

import dev.mednikov.accounting.accounts.models.AccountCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountCategoryRepository extends JpaRepository<AccountCategory, Long> {

    List<AccountCategory> findByOrganizationId(Long organizationId);

    Optional<AccountCategory> findByOrganizationIdAndName(Long organizationId, String name);

}
