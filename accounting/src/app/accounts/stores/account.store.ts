import {patchState, signalStore, withComputed, withMethods, withState} from '@ngrx/signals';
import {computed, inject} from '@angular/core';

import {Account} from '../models/accounts.models';
import {AccountService} from '../services/account';
import {UserOrganizationStore} from '../../organizations/stores/user-organization.store';

interface AccountState {
  areAccountsLoaded: boolean
  accounts: Account[]
  sortOrder: string
  displayedAccountsType: string
}

const initialState: AccountState = {
  areAccountsLoaded: false,
  accounts: [],
  sortOrder: 'name-asc',
  displayedAccountsType: 'ALL'
}

export const AccountStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    const accountService: AccountService = inject(AccountService)
    const userOrganizationStore = inject(UserOrganizationStore)
    return {
      createAccount: (payload: Account) => {
        if (userOrganizationStore.activeOrganization() != undefined) {
          const organizationId = userOrganizationStore.activeOrganization()!.organization.id!
          const account: Account = {
            ...payload,
            organizationId
          }
          accountService.createAccount(account).subscribe({
            next: result => {
              // add an account to accounts
              patchState(store, {areAccountsLoaded: true, accounts: [...store.accounts(), result]})
            }
          })
        }
      },
      updateAccount: (payload: Account) => {
        accountService.updateAccount(payload).subscribe({
          next: result => {
            const currentAccounts = store.accounts().filter(e => e.id! != payload.id!)
            currentAccounts.push(result)
            patchState(store, {accounts: currentAccounts})
          }
        })
      },
      deleteAccount: (id: string) => {
        accountService.deleteAccount(id).subscribe({
          next: result => {
            const currentAccounts = store.accounts().filter(e => e.id! != id)
            patchState(store, {accounts: currentAccounts})
          }
        })
      },
      getAccounts: () => {
        if (userOrganizationStore.activeOrganization() != undefined){
          const organizationId = userOrganizationStore.activeOrganization()!.organization.id!
          accountService.getAccounts(organizationId).subscribe({
            next: result => {
              patchState(store, {accounts: result, areAccountsLoaded: true})
            }
          })
        }
      },
      setDisplayedAccountsType(value: string){
        patchState(store, {displayedAccountsType: value})
      },
      setSortOrder: (value: string) => {
        const items = store.accounts().sort((ac1, ac2) => {
          if (value == 'name-asc'){
            return ac1.name.localeCompare(ac2.name)
          } else {
            return ac2.name.localeCompare(ac1.name)
          }
        })
        patchState(store, {
          sortOrder: value,
          accounts: items
        })
      }
    }
  }),
  withComputed((store) => {
    return {
      displayedAccounts: computed(() => {
        switch(store.displayedAccountsType()){
          case 'ASSET':
            return store.accounts().filter(e => e.accountType == 'ASSET')
          case 'LIABILITY':
            return store.accounts().filter(e => e.accountType == 'LIABILITY')
          case 'EQUITY':
            return store.accounts().filter(e => e.accountType == 'EQUITY')
          case 'INCOME':
            return store.accounts().filter(e => e.accountType == 'INCOME')
          case 'EXPENSE':
            return store.accounts().filter(e => e.accountType == 'EXPENSE')
          default:
            return store.accounts()
        }
      }),
      activeAccounts: computed(() => store.accounts().filter(e => !e.deprecated))
    }
  })
)
