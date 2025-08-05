import {Component, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {AccountStore} from '../../stores/account.store';
import {AccountsList} from '../../components/accounts-list/accounts-list';
import {AccountsTabs} from '../../components/accounts-tabs/accounts-tabs';
import {Header} from '../../../core/components/header/header';

@Component({
  selector: 'app-accounts-view',
  imports: [
    AccountsList,
    AccountsTabs,
    Header,

  ],
  templateUrl: './accounts-view.html',
  styleUrl: './accounts-view.css'
})
export class AccountsView implements OnInit{

  readonly accountStore = inject(AccountStore)
  router: Router = inject(Router)

  ngOnInit() {
    this.accountStore.getAccounts()
  }

  onCreateClicked(){
    this.router.navigateByUrl('/accounts/create')
  }

}
