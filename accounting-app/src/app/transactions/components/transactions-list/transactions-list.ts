import {Component, effect, input} from '@angular/core';
import {CurrencyPipe} from '@angular/common';
import {Transaction} from '../../models/transactions.models';

@Component({
  selector: 'app-transactions-list',
  imports: [CurrencyPipe],
  templateUrl: './transactions-list.html',
  styleUrl: './transactions-list.css'
})
export class TransactionsList {

  transactions = input.required<Transaction[]>()
  totalCredit: number = 0
  totalDebit: number = 0
  balanced: boolean = true

  constructor() {
    effect(() => {
      this.transactions().forEach(tr => {
        this.totalCredit = this.totalCredit += tr.totalCreditAmount!
        this.totalDebit = this.totalDebit += tr.totalDebitAmount!
        this.balanced = this.totalCredit == this.totalDebit
      })
    });
  }

}
