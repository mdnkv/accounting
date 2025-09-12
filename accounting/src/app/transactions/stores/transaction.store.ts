import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {inject} from '@angular/core';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import {Transaction} from '../models/transactions.models';
import {TransactionService} from '../services/transaction';
import {UserOrganizationStore} from '../../organizations/stores/user-organization.store';

interface TransactionState {
  transactions: Transaction[]
  sortOrder: string
  areTransactionsLoaded: boolean
  createError: string | undefined
}

const initialState: TransactionState = {
  transactions: [],
  sortOrder: 'date-desc',
  areTransactionsLoaded: false,
  createError: undefined
}

function getError(err: HttpErrorResponse): string {
  return 'Something went wrong. Please try again later'
}

export const TransactionStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    const transactionService: TransactionService = inject(TransactionService)
    const userOrganizationStore = inject(UserOrganizationStore)
    const router: Router = inject(Router)
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
        if (userOrganizationStore.activeOrganization() != undefined){
          const organizationId = userOrganizationStore.activeOrganization()!.organization.id!
          transactionService.getTransactions(organizationId).subscribe({
            next: result => {
              patchState(store, {
                areTransactionsLoaded: true,
                transactions: result,
                createError: undefined
              })
            }
          })
        }
      },
      createTransaction: (payload: Transaction) => {
        const organizationId = userOrganizationStore.activeOrganization()!.organization.id!
        const transaction: Transaction = {
          ...payload,
          organizationId
        }
        transactionService.createTransaction(transaction).subscribe({
          next: result => {
            patchState(store, {
              transactions: [...store.transactions(), result],
              createError: undefined
            })
            router.navigateByUrl('/transactions')
          },
          error: (err: HttpErrorResponse) => {
            console.log(err)
            const message = getError(err)
            patchState(store, {createError: message})
          }
        })
      }
    }
  })
)
