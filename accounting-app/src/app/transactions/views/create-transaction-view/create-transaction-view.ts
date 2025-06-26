import {Component, inject, signal} from '@angular/core';
import {CurrencyPipe} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';

import {Transaction, TransactionLine} from '../../models/transactions.models';
import {CreateTransactionLineModal} from '../../components/create-transaction-line-modal/create-transaction-line-modal';
import {TransactionLineItem} from '../../components/transaction-line-item/transaction-line-item';

import {TransactionService} from '../../services/transaction';

@Component({
  selector: 'app-create-transaction-view',
  imports: [
    CreateTransactionLineModal,
    TransactionLineItem,
    CurrencyPipe,
    ReactiveFormsModule
  ],
  templateUrl: './create-transaction-view.html',
  styleUrl: './create-transaction-view.css'
})
export class CreateTransactionView {

  formBuilder: FormBuilder = inject(FormBuilder)
  transactionService: TransactionService = inject(TransactionService)
  router: Router = inject(Router)

  form: FormGroup = this.formBuilder.group({
    date: [null, [Validators.required]],
    description: ['', [Validators.required]]
  })

  loading = signal(false)
  transactionsLines: TransactionLine[] = []
  totalCreditAmount: number = 0
  totalDebitAmount: number = 0
  balance: number = 0
  balanced: boolean = true
  currency: string = 'EUR'

  onCreateLine (payload: TransactionLine) {
    this.transactionsLines.push(payload)
    this.totalCreditAmount = this.totalCreditAmount + payload.creditAmount
    this.totalDebitAmount = this.totalDebitAmount + payload.debitAmount
    this.balanced = this.totalCreditAmount == this.totalDebitAmount
    this.balance = Math.abs( this.totalCreditAmount - this.totalDebitAmount)
  }

  onDeleteLine (index: number){}

  submit(){
    this.loading.set(true)
    const organizationId = localStorage.getItem('activeOrganizationId') as string
    const payload: Transaction = {
      organizationId: organizationId,
      lines: this.transactionsLines,
      date: this.form.get('date')?.value,
      description: this.form.get('description')?.value,
      currency: this.currency
    }

    this.transactionService.createTransaction(payload).subscribe({
      next: result => {
        console.log(result)
        this.loading.set(false)
        this.router.navigateByUrl('/transactions')
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
        this.loading.set(false)
      }
    })

  }

  onCancel() {
    if (this.form.touched) {
      if (confirm('Do you want to quit?')){
        this.router.navigateByUrl('/transactions')
      }
    } else {
      this.router.navigateByUrl('/transactions')
    }
  }

}
