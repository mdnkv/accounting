import {patchState, signalStore, withComputed, withMethods, withState} from '@ngrx/signals';
import {computed, inject} from '@angular/core';

import {Currency} from '../models/currencies.models';
import {CurrencyService} from '../services/currency';
import {OrganizationStore} from '../../organizations/stores/organizations.store';

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
    const organizationStore = inject(OrganizationStore)
    return {
      getCurrencies: () => {
        if (organizationStore.activeOrganization() != undefined){
          const activeOrganizationId = organizationStore.activeOrganization()!.id!
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
