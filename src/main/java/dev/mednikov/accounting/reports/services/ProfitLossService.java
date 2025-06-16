package dev.mednikov.accounting.reports.services;

import dev.mednikov.accounting.reports.dto.ProfitLossDto;
import dev.mednikov.accounting.reports.dto.ProfitLossSummaryDto;

import java.time.LocalDate;

public interface ProfitLossService {

    ProfitLossDto getProfitLoss (Long organizationId, LocalDate fromDate, LocalDate toDate);

    ProfitLossSummaryDto getProfitLossSummary (Long organizationId);

}
