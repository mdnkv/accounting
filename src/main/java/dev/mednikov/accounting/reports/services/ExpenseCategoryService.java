package dev.mednikov.accounting.reports.services;

import dev.mednikov.accounting.reports.dto.ExpenseCategoryDto;

import java.util.List;

public interface ExpenseCategoryService {

    List<ExpenseCategoryDto> getExpenseCategories(Long organizationId, int daysCount);

}
