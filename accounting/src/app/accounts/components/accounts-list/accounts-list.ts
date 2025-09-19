import {Component, effect, inject} from '@angular/core';

import {MatDialog} from '@angular/material/dialog';

import {Account} from '../../models/accounts.models';
import {AccountStore} from '../../stores/account.store';
import {EmptyListPlaceholder} from '../../../core/components/empty-list-placeholder/empty-list-placeholder';
import {LoadingPlaceholder} from '../../../core/components/loading-placeholder/loading-placeholder';
import {DeleteDialog} from '../../../core/components/delete-dialog/delete-dialog';
import {AccountCard} from '../account-card/account-card';

@Component({
  selector: 'app-accounts-list',
  imports: [
    EmptyListPlaceholder,
    LoadingPlaceholder,
    AccountCard
  ],
  templateUrl: './accounts-list.html',
  styleUrl: './accounts-list.css'
})
export class AccountsList {

  readonly store = inject(AccountStore)
  dialog: MatDialog = inject(MatDialog)

  onDelete(account: Account) {
    let dialogRef = this.dialog.open(DeleteDialog, {
      width: '400px'
    })
    dialogRef.afterClosed().subscribe(result => {
      // Confirm deletion
      if (result == true){
        this.store.deleteAccount(account.id!)
      }
    })
  }

}
