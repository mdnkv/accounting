import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {inject} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';

import {Organization, UserOrganization} from '../models/organizations.models';
import {UserOrganizationService} from '../services/user-organization';
import {OrganizationService} from '../services/organization';

interface UserOrganizationState {
  isActiveOrganizationLoaded: boolean,
  areOrganizationsLoaded: boolean
  activeOrganization: UserOrganization | undefined
  userOrganizations: UserOrganization[]
  createError: string | undefined
}

const initialState: UserOrganizationState = {
  isActiveOrganizationLoaded: false,
  areOrganizationsLoaded: false,
  activeOrganization: undefined,
  userOrganizations: [],
  createError: undefined
}

export const UserOrganizationStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    const userOrganizationService: UserOrganizationService = inject(UserOrganizationService)
    const organizationService: OrganizationService = inject(OrganizationService)
    const router: Router = inject(Router)
    return {
      createOrganization(payload: Organization) {
        organizationService.createOrganization(payload).subscribe({
          next: result => {
            // user has other organizations
            if (store.activeOrganization() != undefined){
              patchState(store, {createError: undefined})
              router.navigateByUrl('/organizations')
            } else {
              // no other organizations
              // load active organization
              userOrganizationService.getActiveOrganizationForUser().subscribe({
                next: result2 => {
                  // update state
                  patchState(store, {
                    activeOrganization: result2,
                    isActiveOrganizationLoaded: true,
                    createError: undefined
                  })
                  router.navigateByUrl('/dashboard')
                }
              })
            }
          }
        })
      },
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
