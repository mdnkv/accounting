import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {inject} from '@angular/core';
import {RoleService} from '../services/role';
import {Role} from '../models/roles.models';
import {OrganizationStore} from '../../organizations/stores/organizations.store';

interface RoleState {
  roles: Role[]
  areRolesLoaded: boolean
}

const initialState: RoleState = {
  roles: [],
  areRolesLoaded: false
}

export const RoleStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    const roleService = inject(RoleService)
    const organizationStore = inject(OrganizationStore)

    return {
      getRoles: () => {
        if (organizationStore.isActiveOrganizationLoaded()){
          const organizationId = organizationStore.activeOrganization()!.id!
          roleService.getRolesForOrganization(organizationId).subscribe({
            next: result => {
              patchState(store, {roles: result, areRolesLoaded: true})
            }
          })
        }
      }
    }
  })
)
