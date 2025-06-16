package dev.mednikov.accounting.reports.services;

import dev.mednikov.accounting.accounts.models.AccountType;
import dev.mednikov.accounting.organizations.exceptions.OrganizationNotFoundException;
import dev.mednikov.accounting.organizations.models.Organization;
import dev.mednikov.accounting.organizations.repositories.OrganizationRepository;
import dev.mednikov.accounting.reports.dto.NetWorthSummaryDto;
import dev.mednikov.accounting.transactions.models.TransactionLine;
import dev.mednikov.accounting.transactions.repositories.TransactionLineRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class NetWorthServiceImpl implements NetWorthService {

    private final OrganizationRepository organizationRepository;
    private final TransactionLineRepository transactionLineRepository;

    public NetWorthServiceImpl(OrganizationRepository organizationRepository, TransactionLineRepository transactionLineRepository) {
        this.organizationRepository = organizationRepository;
        this.transactionLineRepository = transactionLineRepository;
    }

    @Override
    public NetWorthSummaryDto getNetWorthSummary(Long organizationId) {
        Organization organization = this.organizationRepository.findById(organizationId).orElseThrow(OrganizationNotFoundException::new);
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = toDate.minusDays(30); // default last 30 days

        List<TransactionLine> transactionLines = this.transactionLineRepository.getAssetsAndLiabilityLines(organizationId, fromDate, toDate);
        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal totalLiabilities = BigDecimal.ZERO;

        for (TransactionLine transactionLine : transactionLines) {
            if (transactionLine.getAccount().getAccountType() == AccountType.ASSET) {
                BigDecimal amount = transactionLine.getDebitAmount().subtract(transactionLine.getCreditAmount());
                totalAssets = totalAssets.add(amount);
            } else {
                BigDecimal amount = transactionLine.getCreditAmount().subtract(transactionLine.getDebitAmount());
                totalLiabilities = totalLiabilities.add(amount);
            }
        }
        BigDecimal netWorth = totalAssets.subtract(totalLiabilities);
        return new NetWorthSummaryDto(
                organization.getCurrency(),
                totalAssets,
                totalLiabilities,
                netWorth
        );
    }
}
