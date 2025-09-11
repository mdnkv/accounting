import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {inject} from '@angular/core';
import {Router} from '@angular/router';

import {UserOrganizationStore} from '../../organizations/stores/user-organization.store';
import {JournalService} from '../services/journal';
import {Journal} from '../models/journals.models';

interface JournalState {
  areJournalsLoaded: boolean
  journals: Journal[]
  createError: string | undefined
  updateError: string | undefined
}

const initialState: JournalState = {
  areJournalsLoaded: false,
  journals: [],
  createError: undefined,
  updateError: undefined
}

export const JournalStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    const userOrganizationStore = inject(UserOrganizationStore)
    const journalService: JournalService = inject(JournalService)
    const router: Router = inject(Router)
    return {
      createJournal(payload: Journal) {

        if (userOrganizationStore.activeOrganization() != undefined) {
          const organizationId = userOrganizationStore.activeOrganization()!.organization.id!
          const journal: Journal = {
            ...payload,
            organizationId
          }
          journalService.createJournal(journal).subscribe({
            next: result => {
              // add journal to state
              patchState(store, {
                areJournalsLoaded: true,
                createError: undefined,
                updateError: undefined,
                journals: [...store.journals(), result]
              })
              router.navigateByUrl('/journals')
            }
          })
        }

      },
      updateJournal(payload: Journal) {
        journalService.updateJournal(payload).subscribe({
          next: result => {
            const items = store.journals().filter(e => e.id! != payload.id!)
            items.push(result)
            patchState(store, {
              createError: undefined,
              updateError: undefined,
              journals: items
            })
            router.navigateByUrl('/journals')
          }
        })
      },
      deleteJournal(id: string){
        journalService.deleteJournal(id).subscribe({
          next: result => {
            const items = store.journals().filter(e => e.id! != id)
            patchState(store, {journals: items})
          }
        })
      },
      getJournals() {
        if (userOrganizationStore.activeOrganization() != undefined){
          const organizationId = userOrganizationStore.activeOrganization()!.organization.id!
          journalService.getJournals(organizationId).subscribe({
            next: result => {
              patchState(store, {journals: result, areJournalsLoaded: true})
            }
          })
        }
      }
    }
  })
)
