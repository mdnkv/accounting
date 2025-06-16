package dev.mednikov.accounting.reports.services;

import dev.mednikov.accounting.reports.dto.NetWorthSummaryDto;

public interface NetWorthService {

    NetWorthSummaryDto getNetWorthSummary (Long organizationId);

}
