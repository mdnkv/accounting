import {Component, effect, inject, input} from '@angular/core';
import {CurrencyPipe} from '@angular/common';
import {TransactionStore} from '../../stores/transactions.store';

@Component({
  selector: 'app-transactions-list',
  imports: [CurrencyPipe],
  templateUrl: './transactions-list.html',
  styleUrl: './transactions-list.css'
})
export class TransactionsList {

  readonly transactionStore = inject(TransactionStore)

}
