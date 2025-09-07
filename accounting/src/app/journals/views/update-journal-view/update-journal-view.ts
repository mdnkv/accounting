import {Component, inject, input, OnInit, signal} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {JournalForm} from '../../components/journal-form/journal-form';
import {Journal} from '../../models/journals.models';
import {JournalService} from '../../services/journal';
import {JournalStore} from '../../stores/journal.store';

@Component({
  selector: 'app-update-journal-view',
  imports: [
    JournalForm
  ],
  templateUrl: './update-journal-view.html',
  styleUrl: './update-journal-view.css'
})
export class UpdateJournalView implements OnInit{

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

}
