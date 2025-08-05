import {Component, effect, inject} from '@angular/core';
import {CurrencyPipe, DatePipe} from '@angular/common';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {Transaction} from '../../models/transactions.models';
import {TransactionStore} from '../../stores/transaction.store';
import {EmptyListPlaceholder} from '../../../core/components/empty-list-placeholder/empty-list-placeholder';
import {LoadingPlaceholder} from '../../../core/components/loading-placeholder/loading-placeholder';

@Component({
  selector: 'app-transactions-list',
  imports: [CurrencyPipe, DatePipe, MatTableModule, EmptyListPlaceholder, LoadingPlaceholder],
  templateUrl: './transactions-list.html',
  styleUrl: './transactions-list.css'
})
export class TransactionsList {

  readonly store = inject(TransactionStore)

  displayedColumns = ['date', 'description', 'debitAmount', 'creditAmount']
  dataSource = new MatTableDataSource<Transaction>()

  constructor() {
    effect(() => {
      this.dataSource.data = this.store.transactions()
    })
  }

}
