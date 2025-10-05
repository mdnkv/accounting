import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {inject} from '@angular/core';

import {AccountCategory} from '../models/accounts.models';
import {UserOrganizationStore} from '../../organizations/stores/user-organization.store';
import {AccountCategoryService} from '../services/account-category';

interface AccountCategoryState {
  areAccountCategoriesLoaded: boolean
  categories: AccountCategory[]
}

const initialState: AccountCategoryState = {
  areAccountCategoriesLoaded: false,
  categories: [],
}

export const AccountCategoryStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    const userOrganizationStore = inject(UserOrganizationStore)
    const accountCategoryService = inject(AccountCategoryService)
    return {
      getAccountCategories: () => {
        if (userOrganizationStore.activeOrganization() != undefined){
          const organizationId = userOrganizationStore.activeOrganization()!.organization.id!
          accountCategoryService.getAccountCategories(organizationId).subscribe({
            next: result => {
              patchState(store, {
                categories: result,
                areAccountCategoriesLoaded: true
              })
            }
          })
        }
      },
      //
    }
  })
)
