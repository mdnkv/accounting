import {inject} from '@angular/core';
import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {RoleService} from '../services/role';
import {Role} from '../models/roles.models';
import {UserOrganizationStore} from '../../organizations/stores/user-organization.store';

interface RoleStoreState {
  roles: Role[]
  isLoaded: boolean
}

const initialState: RoleStoreState  = {
  roles: [],
  isLoaded: false
}

export const RoleStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    const roleService: RoleService = inject(RoleService)
    const userOrganizationStore = inject(UserOrganizationStore)

    return {

      getRoles(){
        if (userOrganizationStore.activeOrganization() != undefined) {
          const activeOrganizationId = userOrganizationStore.activeOrganization()!.organization.id!
          roleService.getRoles(activeOrganizationId).subscribe({
            next: result => {
              patchState(store, {
                roles: result,
                isLoaded: true
              })
            },
            error: (err) => {
              console.log(err)
            }
          })
        }
      }

    }
  })
)
