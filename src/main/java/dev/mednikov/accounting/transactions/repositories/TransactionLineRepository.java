package dev.mednikov.accounting.transactions.repositories;

import dev.mednikov.accounting.transactions.models.TransactionLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionLineRepository extends JpaRepository<TransactionLine, Long> {

    @Query("""
    SELECT tl FROM TransactionLine tl
    WHERE tl.transaction.organization.id = :organizationId
    AND tl.account.accountType IN ('ASSET', 'LIABILITY', 'EQUITY')
    AND tl.transaction.date <= :toDate ORDER BY tl.account.accountType ASC
    """)
    List<TransactionLine> getBalanceSheetLines (Long organizationId, LocalDate toDate);

    @Query("""
    SELECT tl FROM TransactionLine tl
    WHERE tl.transaction.organization.id = :organizationId
    AND (tl.transaction.date >= :fromDate AND tl.transaction.date <= :toDate)
    AND tl.account.accountType IN ('INCOME', 'EXPENSE')
    """)
    List<TransactionLine> getProfitLossLines(Long organizationId, LocalDate fromDate, LocalDate toDate);

    @Query("""
    SELECT tl FROM TransactionLine tl
    WHERE tl.transaction.organization.id = :organizationId
    AND tl.account.accountType IN ('ASSET', 'LIABILITY')
    AND (tl.transaction.date >= :fromDate AND tl.transaction.date <= :toDate)
    """)
    List<TransactionLine> getAssetsAndLiabilityLines (Long organizationId, LocalDate fromDate, LocalDate toDate);

    @Query("""
    SELECT tl FROM TransactionLine tl
    WHERE tl.transaction.organization.id = :organizationId
    AND tl.account.accountType = 'EXPENSE'
    AND (tl.transaction.date >= :fromDate AND tl.transaction.date <= :toDate)
    """)
    List<TransactionLine> getExpenseLines (Long organizationId, LocalDate fromDate, LocalDate toDate);


}
