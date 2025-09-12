import {Component, inject} from '@angular/core';
import {JournalForm} from '../../components/journal-form/journal-form';
import {Journal} from '../../models/journals.models';
import {JournalStore} from '../../stores/journal.store';
import {Router} from '@angular/router';
import {ErrorMessage} from '../../../core/components/error-message/error-message';

@Component({
  selector: 'app-create-journal-view',
  imports: [
    JournalForm,
    ErrorMessage
  ],
  templateUrl: './create-journal-view.html',
  styleUrl: './create-journal-view.css'
})
export class CreateJournalView {

  router: Router = inject(Router)
  readonly journalStore = inject(JournalStore)

  onCreateJournal(payload: Journal) {
    this.journalStore.createJournal(payload)
  }

  onCancel(){
    this.router.navigateByUrl('/journals')
  }

}
