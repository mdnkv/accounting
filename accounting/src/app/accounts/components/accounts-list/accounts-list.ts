import {Component, effect, inject} from '@angular/core';
import {Router} from '@angular/router';

import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {MatMenuModule} from '@angular/material/menu';

import {Account} from '../../models/accounts.models';
import {AccountStore} from '../../stores/account.store';
import {EmptyListPlaceholder} from '../../../core/components/empty-list-placeholder/empty-list-placeholder';
import {LoadingPlaceholder} from '../../../core/components/loading-placeholder/loading-placeholder';

@Component({
  selector: 'app-accounts-list',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatMenuModule,
    EmptyListPlaceholder,
    LoadingPlaceholder
  ],
  templateUrl: './accounts-list.html',
  styleUrl: './accounts-list.css'
})
export class AccountsList {

  readonly store = inject(AccountStore)
  router: Router = inject(Router)

  displayedColumns = ["actions", "code", "name", "type"]
  dataSource: MatTableDataSource<Account> = new MatTableDataSource()

  constructor() {
    effect(() => {
      this.dataSource.data = this.store.displayedAccounts()
    })
  }

  onDelete(account: Account) {
    if (confirm('Do you want to remove this account?')) {
      this.store.deleteAccount(account.id!)
    }
  }

  onUpdate (account: Account) {
    this.router.navigate(['/accounts/update', account.id!])
  }

}
