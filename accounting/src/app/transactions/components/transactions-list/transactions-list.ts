import {Component, inject} from '@angular/core';
import {CurrencyPipe, DatePipe} from '@angular/common';
import {TransactionStore} from '../../stores/transaction.store';
import {EmptyListPlaceholder} from '../../../core/components/empty-list-placeholder/empty-list-placeholder';
import {LoadingPlaceholder} from '../../../core/components/loading-placeholder/loading-placeholder';

@Component({
  selector: 'app-transactions-list',
  imports: [CurrencyPipe, DatePipe,  EmptyListPlaceholder, LoadingPlaceholder],
  templateUrl: './transactions-list.html',
  styleUrl: './transactions-list.css'
})
export class TransactionsList {

  readonly store = inject(TransactionStore)

}
