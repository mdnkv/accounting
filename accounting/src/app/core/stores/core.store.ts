import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';

interface CoreState {
  isAuthenticated: boolean
  isSideMenuOpened: boolean
}

const initialState: CoreState = {
  isAuthenticated: true,
  isSideMenuOpened: true
}

export const CoreStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    return {

      toggleSideMenu: () => {
        patchState(store, {
          isSideMenuOpened: !store.isSideMenuOpened()
        })
      }

    }
  })
)
