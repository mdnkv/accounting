import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {inject} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {UserOrganization} from '../models/organizations.models';
import {UserOrganizationService} from '../services/user-organization';

interface UserOrganizationState {
  isActiveOrganizationLoaded: boolean,
  areOrganizationsLoaded: boolean
  activeOrganization: UserOrganization | undefined
  userOrganizations: UserOrganization[]
}

const initialState: UserOrganizationState = {
  isActiveOrganizationLoaded: false,
  areOrganizationsLoaded: false,
  activeOrganization: undefined,
  userOrganizations: []
}

export const UserOrganizationStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    const userOrganizationService: UserOrganizationService = inject(UserOrganizationService)

    return {

      loadActiveOrganization: () => {
        userOrganizationService.getActiveOrganizationForUser().subscribe({
          next: result => {
            // active organization found
            patchState(store, {
              activeOrganization: result,
              isActiveOrganizationLoaded: true
            })
          },
          error: (err: HttpErrorResponse) => {
            if (err.status == 404){
              // no active organization
              patchState(store, {activeOrganization: undefined, isActiveOrganizationLoaded: true})
            }
          }
        })
      },
      setActiveOrganization: (uo: UserOrganization) => {
        userOrganizationService.setActiveOrganizationForUser(uo.id).subscribe({
          next: result => {
            patchState(store, {
              activeOrganization: result,
              isActiveOrganizationLoaded: true,
              userOrganizations: store.userOrganizations().map(or => {
                or.active = or.organization.id! == uo.organization.id!
                return or
              })
            })
          },
          error: (err: HttpErrorResponse) => {
            console.log(err)
          }
        })
      },
      getUserOrganizations: () => {
        userOrganizationService.getOrganizationsForUser().subscribe({
          next: result => {
            patchState(store, {
              userOrganizations: result,
              areOrganizationsLoaded: true
            })
          }
        })
      }

    }
  })
)
