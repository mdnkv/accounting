export interface ProfitLossSummary {
  currency: string
  totalIncome: number
  totalExpenses: number
  netProfit: number
}

export interface NetWorthSummary {
  currency: string
  totalAssets: number
  totalLiabilities: number
  netWorth: number
}

export interface ExpenseCategory {
  name: string
  amount: number
}
