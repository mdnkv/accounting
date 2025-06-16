package dev.mednikov.accounting.reports.utils;

import dev.mednikov.accounting.reports.dto.ExpenseCategoryDto;

import java.util.Comparator;


public final class ExpenseCategoryComparator implements Comparator<ExpenseCategoryDto> {

    @Override
    public int compare(ExpenseCategoryDto o1, ExpenseCategoryDto o2) {
        return o2.amount().compareTo(o1.amount());
    }
}
