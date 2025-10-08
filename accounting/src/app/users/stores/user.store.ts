import {inject} from '@angular/core';
import {patchState, signalStore, withComputed, withMethods, withState} from '@ngrx/signals';
import {User} from '../models/users.models';
import {UserService} from '../services/user';

interface UserState {
  currentUser: User | undefined
}

const initialState : UserState = {
  currentUser: undefined
}

export const UserStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods(store => {
    const service: UserService = inject(UserService)
    return {
      loadCurrentUser() {
        service.getCurrentUser().subscribe({
          next: result => {
            patchState(store, {currentUser: result})
          }
        })
      }
    }
  }),
  withComputed(store => {
    return {
      getDisplayedName() {
        if (store.currentUser() != undefined){
          return store.currentUser()!.firstName
        } else {
          return 'User'
        }
      },
      getGravatar(){
        if (store.currentUser() != undefined){
          return store.currentUser()!.gravatarUrl
        } else {
          return ''
        }
      }
    }
  })
)
