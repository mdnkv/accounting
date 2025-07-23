import {Component, inject, output} from '@angular/core';
import {TransactionStore} from '../../stores/transactions.store';

@Component({
  selector: 'app-sort-transactions-dropdown',
  imports: [],
  templateUrl: './sort-transactions-dropdown.html',
  styleUrl: './sort-transactions-dropdown.css'
})
export class SortTransactionsDropdown {

  readonly transactionStore = inject(TransactionStore)

  isActive: boolean = false

  selectSortOrder (order: string){
    this.isActive = false
    this.transactionStore.setSortOrder(order)
  }

}
