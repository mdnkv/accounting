import {Component, inject, OnInit} from '@angular/core';
import {TransactionStore} from '../../stores/transaction.store';
import {TransactionsList} from '../../components/transactions-list/transactions-list';
import {Header} from '../../../core/components/header/header';
import {Router} from '@angular/router';

@Component({
  selector: 'app-transactions-view',
  imports: [TransactionsList, Header],
  templateUrl: './transactions-view.html',
  styleUrl: './transactions-view.css'
})
export class TransactionsView implements OnInit{

  readonly store = inject(TransactionStore)
  router: Router = inject(Router)

  ngOnInit() {
    this.store.getTransactions()
  }

  onCreateClicked(){
    this.router.navigateByUrl('/transactions/create')
  }

}
