import {Component, inject} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';

import {EmptyListPlaceholder} from '../../../core/components/empty-list-placeholder/empty-list-placeholder';
import {LoadingPlaceholder} from '../../../core/components/loading-placeholder/loading-placeholder';
import {JournalStore} from '../../stores/journal.store';
import {Journal} from '../../models/journals.models';
import {DeleteDialog} from '../../../core/components/delete-dialog/delete-dialog';
import {JournalCard} from '../journal-card/journal-card';

@Component({
  selector: 'app-journals-list',
  imports: [
    EmptyListPlaceholder,
    LoadingPlaceholder,
    JournalCard
  ],
  templateUrl: './journals-list.html',
  styleUrl: './journals-list.css'
})
export class JournalsList {

  readonly store = inject(JournalStore)
  dialog: MatDialog = inject(MatDialog)

  onDelete(journal: Journal) {
    let dialogRef = this.dialog.open(DeleteDialog, {
      width: '400px'
    })
    dialogRef.afterClosed().subscribe(result => {
      // Confirm deletion
      if (result == true){
        this.store.deleteJournal(journal.id!)
      }
    })
  }

}
