import {Component, effect, inject, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';

import {SortTransactionsDropdown} from '../../components/sort-transactions-dropdown/sort-transactions-dropdown';
import {TransactionsList} from '../../components/transactions-list/transactions-list';
import {TransactionStore} from '../../stores/transactions.store';
import {OrganizationStore} from '../../../organizations/stores/organizations.store';

@Component({
  selector: 'app-transactions-view',
  imports: [
    SortTransactionsDropdown,
    TransactionsList,
    RouterLink
  ],
  templateUrl: './transactions-view.html',
  styleUrl: './transactions-view.css'
})
export class TransactionsView implements OnInit{

  readonly organizationStore = inject(OrganizationStore)
  readonly transactionStore = inject(TransactionStore)

  ngOnInit() {
    this.organizationStore.getActiveOrganization()
  }

  constructor() {
    effect(() => {
      if (this.organizationStore.isActiveOrganizationLoaded()){
        this.transactionStore.getTransactions()
      }
    })
  }

}
