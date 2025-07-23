import {Component, effect, inject, OnInit} from '@angular/core';

import {CreateAccountModal} from '../../components/create-account-modal/create-account-modal';
import {SortAccountDropdown} from '../../components/sort-account-dropdown/sort-account-dropdown';
import {AccountsTabs} from '../../components/accounts-tabs/accounts-tabs';
import {Account} from '../../models/accounts.models';
import {AccountsList} from '../../components/accounts-list/accounts-list';
import {AccountStore} from '../../stores/accounts.store';
import {OrganizationStore} from '../../../organizations/stores/organizations.store';

@Component({
  selector: 'app-accounts-view',
  imports: [
    CreateAccountModal,
    SortAccountDropdown,
    AccountsTabs,
    AccountsList
  ],
  templateUrl: './accounts-view.html',
  styleUrl: './accounts-view.css'
})
export class AccountsView implements OnInit{

  readonly organizationStore = inject(OrganizationStore)
  readonly accountStore = inject(AccountStore)

  onCreateAccount(payload: Account) {
    this.accountStore.createAccount(payload)
  }


  constructor() {
    effect(() => {
      if (this.organizationStore.isActiveOrganizationLoaded()){
        this.accountStore.getAccounts()
      }
    })
  }

  ngOnInit() {
    this.organizationStore.getActiveOrganization()
  }

}
