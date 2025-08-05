import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {computed, inject} from '@angular/core';

import {Currency} from '../models/currencies.models';
import {CurrencyService} from '../services/currency';
import {UserOrganizationStore} from '../../organizations/stores/user-organization.store';

interface CurrencyState {
  primaryCurrency: Currency | undefined
  currencies: Currency[]
  areCurrenciesLoaded: boolean
  isPrimaryCurrencyLoaded: boolean
}

const initialState : CurrencyState = {
  primaryCurrency: undefined,
  currencies: [],
  areCurrenciesLoaded: false,
  isPrimaryCurrencyLoaded: false
}

export const CurrencyStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    const currencyService: CurrencyService = inject(CurrencyService)
    const userOrganizationStore = inject(UserOrganizationStore)
    return {
      getCurrencies: () => {
        if (userOrganizationStore.activeOrganization() != undefined){
          const activeOrganizationId = userOrganizationStore.activeOrganization()!.organization.id!
          currencyService.getCurrencies(activeOrganizationId).subscribe({
            next: result => {

              const primaryCurrency: Currency = result.filter(c => c.primary)[0]

              patchState(store, {
                areCurrenciesLoaded: true,
                currencies: result,
                primaryCurrency: primaryCurrency,
                isPrimaryCurrencyLoaded: true
              })
            }
          })
        }
      }

    }
  })
)
