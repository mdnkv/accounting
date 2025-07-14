import {Component, inject, OnInit, signal} from '@angular/core';
import {CurrencyPipe} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';

import {Transaction, TransactionLine} from '../../models/transactions.models';
import {CreateTransactionLineModal} from '../../components/create-transaction-line-modal/create-transaction-line-modal';
import {TransactionLineItem} from '../../components/transaction-line-item/transaction-line-item';

import {TransactionService} from '../../services/transaction';
import {CurrencyService} from '../../../currencies/services/currency';
import {Currency} from '../../../currencies/models/currencies.models';

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
export class CreateTransactionView implements OnInit{

  formBuilder: FormBuilder = inject(FormBuilder)
  transactionService: TransactionService = inject(TransactionService)
  currencyService: CurrencyService = inject(CurrencyService)
  router: Router = inject(Router)

  form: FormGroup = this.formBuilder.group({
    date: [null, [Validators.required]],
    description: ['', [Validators.required]],
    currencyId: [null, [Validators.required]]
  })

  loading = signal(false)
  transactionsLines: TransactionLine[] = []
  currencies: Currency[] = []
  primaryCurrencyCode: string = 'EUR'
  totalCreditAmount: number = 0
  totalDebitAmount: number = 0
  balance: number = 0
  balanced: boolean = true

  ngOnInit() {
    this.currencyService.getCurrencies().subscribe({
      next: result => {
        this.currencies = result
        if (this.currencies.length > 0){
          const primaryCurrency = this.currencies.filter(e=>e.primary)[0]
          this.primaryCurrencyCode = primaryCurrency.code
          this.form.get('currencyId')?.setValue(primaryCurrency.id!)
        }
      }
    })
  }

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
      currencyId: this.form.get('currencyId')?.value
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
