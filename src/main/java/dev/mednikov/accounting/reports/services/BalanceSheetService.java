package dev.mednikov.accounting.reports.services;

import dev.mednikov.accounting.reports.dto.BalanceSheetDto;

import java.time.LocalDate;

public interface BalanceSheetService {

    BalanceSheetDto getBalanceSheet (Long organizationId, LocalDate date);

}
