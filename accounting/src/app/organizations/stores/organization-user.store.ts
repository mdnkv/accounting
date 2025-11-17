import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {inject} from '@angular/core';
import {OrganizationUserService} from '../services/organization-user';
import {UserOrganizationStore} from './user-organization.store';
import {CreateOrganizationUserRequest, OrganizationUser} from '../models/organizations.models';

interface OrganizationUserState {
  users: OrganizationUser[]
  isLoaded: boolean
  isShowModal: boolean
}

const initialState: OrganizationUserState = {
  users: [],
  isLoaded: false,
  isShowModal: false
}

export const OrganizationUserStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    const organizationUserService:OrganizationUserService = inject(OrganizationUserService)
    const userOrganizationStore = inject(UserOrganizationStore)

    return {

      showModal(value: boolean){
        patchState(store, {isShowModal: value})
      },

      createOrganizationUser(payload: CreateOrganizationUserRequest) {
        // get active organization id
        const activeOrganizationId = userOrganizationStore.activeOrganization()!.organization.id!
        payload.organizationId = activeOrganizationId
        organizationUserService.createOrganizationUser(payload).subscribe({
          next: result => {
            console.log(result)
            if (result != null){
              // user created
              let users = store.users()
              users.push(result)
              patchState(store, {users: users})
            } else {
              // invitation created
              alert('The invitation was sent to the user')
            }
          },
          error: (err) => {
            console.log(err)
          }
        })
      },

      updateOrganizationUser(payload: OrganizationUser) {
        organizationUserService.updateOrganizationUser(payload).subscribe({
          next: result => {
            const users = store.users().filter(e => e.id! != payload.id!)
            users.push(result)
            patchState(store, {
              users: users
            })
          },
          error: (err) => {
            console.log(err)
          }
        })
      },

      getUsers(){
        if (userOrganizationStore.activeOrganization() != undefined) {
          const activeOrganizationId = userOrganizationStore.activeOrganization()!.organization.id!
          organizationUserService.getUsersInOrganization(activeOrganizationId).subscribe({
            next: (result) => {
              patchState(store, {
                users: result,
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
