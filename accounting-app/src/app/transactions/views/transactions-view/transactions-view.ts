import {Component, inject, OnInit} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {RouterLink} from '@angular/router';

import {SortTransactionsDropdown} from '../../components/sort-transactions-dropdown/sort-transactions-dropdown';
import {TransactionsList} from '../../components/transactions-list/transactions-list';
import {Transaction} from '../../models/transactions.models';
import {TransactionService} from '../../services/transaction';

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

  transactions: Transaction[] = []
  transactionService: TransactionService = inject(TransactionService)

  onSortTransactions(order: string){}

  ngOnInit() {
    this.transactionService.getTransactions().subscribe({
      next: result => {
        this.transactions = result
      },
      error: (err: HttpErrorResponse)=> {
        console.log(err)
      }
    })
  }

}
