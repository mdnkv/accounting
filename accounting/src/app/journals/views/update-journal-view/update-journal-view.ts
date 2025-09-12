import {Component, inject, input, OnInit, signal} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {JournalForm} from '../../components/journal-form/journal-form';
import {Journal} from '../../models/journals.models';
import {JournalService} from '../../services/journal';
import {JournalStore} from '../../stores/journal.store';
import {Router} from '@angular/router';
import {ErrorMessage} from '../../../core/components/error-message/error-message';

@Component({
  selector: 'app-update-journal-view',
  imports: [
    JournalForm,
    ErrorMessage
  ],
  templateUrl: './update-journal-view.html',
  styleUrl: './update-journal-view.css'
})
export class UpdateJournalView implements OnInit{

  router: Router = inject(Router)

  journalService: JournalService = inject(JournalService)
  readonly journalStore = inject(JournalStore)

  id = input.required<string>()
  currentJournal = signal<Journal|undefined>(undefined)

  ngOnInit() {
    this.journalService.getJournal(this.id()).subscribe({
      next: result => {
        this.currentJournal.set(result)
      },
      error: (err: HttpErrorResponse)=> {
        console.log(err)
      }
    })
  }

  onUpdateJournal(payload: Journal) {
    this.journalStore.updateJournal(payload)
  }

  onCancel(){
    this.router.navigateByUrl('/journals')
  }

}
