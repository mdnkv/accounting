import {Component, inject} from '@angular/core';
import {JournalForm} from '../../components/journal-form/journal-form';
import {Journal} from '../../models/journals.models';
import {JournalStore} from '../../stores/journal.store';

@Component({
  selector: 'app-create-journal-view',
  imports: [
    JournalForm
  ],
  templateUrl: './create-journal-view.html',
  styleUrl: './create-journal-view.css'
})
export class CreateJournalView {

  readonly journalStore = inject(JournalStore)

  onCreateJournal(payload: Journal) {
    this.journalStore.createJournal(payload)
  }

}
