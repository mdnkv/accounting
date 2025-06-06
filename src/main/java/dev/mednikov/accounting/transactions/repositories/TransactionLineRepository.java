package dev.mednikov.accounting.transactions.repositories;

import dev.mednikov.accounting.transactions.models.TransactionLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLineRepository extends JpaRepository<TransactionLine, Long> {
}
