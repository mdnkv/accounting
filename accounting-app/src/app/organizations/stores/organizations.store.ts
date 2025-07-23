import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {inject} from '@angular/core';

import {HttpErrorResponse} from '@angular/common/http';

import {Organization, UserOrganization} from '../models/organizations.models';
import {OrganizationUserService} from '../services/organization-user';

interface OrganizationState {
  activeOrganization: Organization | undefined
  organizations: UserOrganization[]
  isActiveOrganizationLoaded: boolean
  areUserOrganizationsLoaded: boolean
}

const initialState: OrganizationState = {
  activeOrganization: undefined,
  organizations: [],
  isActiveOrganizationLoaded: false,
  areUserOrganizationsLoaded: false
}

export const OrganizationStore = signalStore(
  {providedIn:'root'},
  withState(initialState),
  withMethods((store) => {
    const organizationUserService = inject(OrganizationUserService)
    return {
      getActiveOrganization: () => {
        console.log('Load active organization from server')
        organizationUserService.getActiveForUser().subscribe({
          next: result => {
            // active organization found
            patchState(store, {
              activeOrganization: result.organization,
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
        // if (store.activeOrganization == undefined){
        //   // load only if no active organization
        //
        // } else {
        //   console.log('Cached active organization called')
        // }
      },
      setActiveOrganization: (id: string) => {
        organizationUserService.setActiveForUser(id).subscribe({
          next: result => {
            patchState(store, {
              activeOrganization: result.organization,
              isActiveOrganizationLoaded: true,
              organizations: store.organizations().map(organization => {
                organization.active = organization.id == id;
                return organization
              })
            })
          },
          error: (err: HttpErrorResponse) => {
            console.log(err)
          }
        })
      },
      getOrganizationsForUser: () => {
        organizationUserService.getAllForUser().subscribe({
          next: result => {
            patchState(store, {
              organizations: result,
              areUserOrganizationsLoaded: true
            })
          }
        })
      }
    }
  })
)
