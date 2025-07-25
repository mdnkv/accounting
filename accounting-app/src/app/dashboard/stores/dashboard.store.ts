import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {inject} from '@angular/core';
import {ExpenseCategory, NetWorthSummary, ProfitLossSummary} from '../../reports/models/reports.models';
import {ReportService} from '../../reports/services/report';
import {OrganizationStore} from '../../organizations/stores/organizations.store';

interface DashboardState {
  netWorthSummary: NetWorthSummary
  profitLossSummary: ProfitLossSummary
  expenseCategories: ExpenseCategory[]
  netWorthDaysCount: number
  profitLossDaysCount: number
  expenseCategoriesDaysCount: number
}

const initialState : DashboardState = {
  netWorthSummary: {
    netWorth: 0.0,
    totalLiabilities: 0.0,
    totalAssets: 0.0
  },
  profitLossSummary: {
    totalExpenses: 0.0,
    totalIncome: 0.0,
    netProfit: 0.0
  },
  expenseCategories: [],
  netWorthDaysCount: 30,
  profitLossDaysCount: 30,
  expenseCategoriesDaysCount: 30
}

export const DashboardStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    const reportService: ReportService = inject(ReportService)
    const organizationStore = inject(OrganizationStore)

    return {
      refreshProfitLossSummary: (daysCount: number) => {
        if (organizationStore.activeOrganization() != undefined){
          const organizationId = organizationStore.activeOrganization()!.id!
          reportService.getProfitLossSummary(organizationId, daysCount).subscribe({
            next: result => {
              patchState(store, {
                profitLossDaysCount: daysCount,
                profitLossSummary: result
              })
            }
          })
        }
      },
      refreshNetWorthSummary: (daysCount: number) => {
        if (organizationStore.activeOrganization() != undefined){
          const organizationId = organizationStore.activeOrganization()!.id!
          reportService.getNetWorthSummary(organizationId, daysCount).subscribe({
            next: result => {
              patchState(store, {
                netWorthSummary: result,
                netWorthDaysCount: daysCount
              })
            }
          })
        }
      },
      refreshExpenseCategories: (daysCount: number) => {
        if (organizationStore.activeOrganization() != undefined){
          const organizationId = organizationStore.activeOrganization()!.id!
          reportService.getExpenseCategories(organizationId, daysCount).subscribe({
            next: result => {
              patchState(store, {
                expenseCategoriesDaysCount: daysCount,
                expenseCategories: result
              })
            }
          })
        }
      },
    }
  })
)
