import {Account} from '../../accounts/models/accounts.models';
import {Currency} from '../../currencies/models/currencies.models';

export interface TransactionLine {
  debitAmount: number
  creditAmount: number
  accountId: string
  account?: Account
  id?: string
}

export interface Transaction {
  date: Date
  description: string
  organizationId?: string
  lines: TransactionLine[]
  currencyId: string
  currency?: Currency
  totalCreditAmount?: number
  totalDebitAmount?: number
  id?: string
}
