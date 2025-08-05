export interface ProfitLossSummary {
  totalIncome: number
  totalExpenses: number
  netProfit: number
}

export interface NetWorthSummary {
  totalAssets: number
  totalLiabilities: number
  netWorth: number
}

export interface ExpenseCategory {
  name: string
  amount: number
}
