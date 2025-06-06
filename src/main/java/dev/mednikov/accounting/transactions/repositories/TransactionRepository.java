package dev.mednikov.accounting.transactions.repositories;

import dev.mednikov.accounting.transactions.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.organization.id = :organizationId ORDER BY t.date DESC")
    List<Transaction> findAllByOrganizationId (Long organizationId);

}
