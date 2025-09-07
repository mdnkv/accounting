import {Component, effect, inject} from '@angular/core';
import {Router} from '@angular/router';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {MatMenuModule} from '@angular/material/menu';
import {EmptyListPlaceholder} from '../../../core/components/empty-list-placeholder/empty-list-placeholder';
import {LoadingPlaceholder} from '../../../core/components/loading-placeholder/loading-placeholder';
import {JournalStore} from '../../stores/journal.store';
import {Journal} from '../../models/journals.models';

@Component({
  selector: 'app-journals-list',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatMenuModule,
    EmptyListPlaceholder,
    LoadingPlaceholder
  ],
  templateUrl: './journals-list.html',
  styleUrl: './journals-list.css'
})
export class JournalsList {

  readonly store = inject(JournalStore)

  router: Router = inject(Router)

  displayedColumns = ["actions", "name"]
  dataSource: MatTableDataSource<Journal> = new MatTableDataSource()

  constructor() {
    effect(() => {
      this.dataSource.data = this.store.journals()
    })
  }

  onDelete(journal: Journal) {
    if (confirm('Do you want to remove this journal?')) {
      this.store.deleteJournal(journal.id!)
    }
  }

  onUpdate(journal: Journal) {
    this.router.navigate(['/journals/update', journal.id!])
  }

}
