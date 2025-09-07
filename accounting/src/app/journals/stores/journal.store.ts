import {patchState, signalStore, withMethods, withState} from '@ngrx/signals';
import {inject} from '@angular/core';
import {UserOrganizationStore} from '../../organizations/stores/user-organization.store';
import {JournalService} from '../services/journal';
import {Journal} from '../models/journals.models';

interface JournalState {
  areJournalsLoaded: boolean
  journals: Journal[]
}

const initialState: JournalState = {
  areJournalsLoaded: false,
  journals: []
}

export const JournalStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => {
    const userOrganizationStore = inject(UserOrganizationStore)
    const journalService: JournalService = inject(JournalService)
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
              patchState(store, {areJournalsLoaded: true, journals: [...store.journals(), result]})
            }
          })
        }

      },
      updateJournal(payload: Journal) {
        journalService.updateJournal(payload).subscribe({
          next: result => {
            const items = store.journals().filter(e => e.id! != payload.id!)
            items.push(result)
            patchState(store, {journals: items})
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
