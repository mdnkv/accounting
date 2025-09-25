import {Account} from '../../accounts/models/accounts.models';
import {Currency} from '../../currencies/models/currencies.models';

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

export interface BalanceSheetLine {
  account: Account
  debitAmount: number
  creditAmount: number
}

export interface BalanceSheet {
  totalCreditAmount: number
  totalDebitAmount: number
  items: BalanceSheetLine[]
  currency: Currency
  date: Date
}
