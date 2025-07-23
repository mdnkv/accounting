import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {inject} from '@angular/core';
import {Transaction} from '../models/transactions.models';
import {TransactionService} from '../services/transaction';
import {OrganizationStore} from '../../organizations/stores/organizations.store';

interface TransactionState {
  transactions: Transaction[]
  sortOrder: string
  areTransactionsLoaded: boolean
}

const initialState: TransactionState = {
  transactions: [],
  sortOrder: 'date-desc',
  areTransactionsLoaded: false,
}

export const TransactionStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    const transactionService: TransactionService = inject(TransactionService)
    const organizationStore = inject(OrganizationStore)
    return {
      setSortOrder: (order: string) => {
        const items = store.transactions().sort((transaction1, transaction2) => {

          switch(order){
            case 'date-asc':
              if (transaction1.date > transaction2.date){
                return 1
              } else if (transaction1.date == transaction2.date) {
                return 0
              } else {
                return -1
              }
            case 'amount-asc':
              return transaction1.totalCreditAmount! - transaction2.totalCreditAmount!
            case 'amount-desc':
              return transaction2.totalCreditAmount! - transaction1.totalCreditAmount!
            default:
              // date descending default
              if (transaction2.date > transaction1.date){
                return 1
              } else if (transaction1.date == transaction2.date) {
                return 0
              } else {
                return -1
              }
          }

        })
        patchState(store, {
          sortOrder: order,
          transactions: items
        })
      },
      getTransactions: () => {
        if (organizationStore.activeOrganization() != undefined){
          const organizationId = organizationStore.activeOrganization()!.id!
          transactionService.getTransactions(organizationId).subscribe({
            next: result => {
              patchState(store, {
                areTransactionsLoaded: true,
                transactions: result
              })
            }
          })
        }
      },
      createTransaction: (payload: Transaction) => {
        const organizationId = organizationStore.activeOrganization()!.id!
        const transaction: Transaction = {
          ...payload,
          organizationId
        }
        transactionService.createTransaction(transaction).subscribe({
          next: result => {
            //
          }
        })
      }
    }
  })
)
