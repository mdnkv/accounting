import {Account} from '../../accounts/models/accounts.models';

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
  organizationId: string
  lines: TransactionLine[]
  currency: string
  totalCreditAmount?: number
  totalDebitAmount?: number
  id?: string
}
