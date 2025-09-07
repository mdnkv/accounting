import {Component, inject, OnInit} from '@angular/core';
import {Header} from "../../../core/components/header/header";
import {JournalsList} from '../../components/journals-list/journals-list';
import {Router} from '@angular/router';
import {JournalStore} from '../../stores/journal.store';

@Component({
  selector: 'app-journals-view',
  imports: [
    Header,
    JournalsList
  ],
  templateUrl: './journals-view.html',
  styleUrl: './journals-view.css'
})
export class JournalsView implements OnInit{

  readonly journalStore = inject(JournalStore)
  router: Router = inject(Router)

  ngOnInit() {
    this.journalStore.getJournals()
  }

  onCreateClicked() {
    this.router.navigateByUrl('/journals/create')
  }

}
