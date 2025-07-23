import {Component, effect, inject, OnInit, signal} from '@angular/core';
import {CurrencyPipe} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';

import {Transaction, TransactionLine} from '../../models/transactions.models';
import {CreateTransactionLineModal} from '../../components/create-transaction-line-modal/create-transaction-line-modal';
import {TransactionLineItem} from '../../components/transaction-line-item/transaction-line-item';

import {CurrencyStore} from '../../../currencies/stores/currencies.store';
import {TransactionStore} from '../../stores/transactions.store';
import {OrganizationStore} from '../../../organizations/stores/organizations.store';
import {AccountStore} from '../../../accounts/stores/accounts.store';

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

  readonly accountStore = inject(AccountStore)
  readonly organizationStore = inject(OrganizationStore)
  readonly currencyStore = inject(CurrencyStore)
  readonly transactionStore = inject(TransactionStore)

  formBuilder: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)

  form: FormGroup = this.formBuilder.group({
    date: [null, [Validators.required]],
    description: ['', [Validators.required]],
    currencyId: [null, [Validators.required]]
  })

  transactionsLines: TransactionLine[] = []
  totalCreditAmount: number = 0
  totalDebitAmount: number = 0
  balance: number = 0
  balanced: boolean = true
  displayedCurrency = signal<string>('EUR')

  constructor() {
    effect(() => {
      if (this.organizationStore.isActiveOrganizationLoaded()){
        this.currencyStore.getCurrencies()
        this.accountStore.getAccounts()
      }
    })
    effect(() => {
      if (this.currencyStore.primaryCurrency() != undefined){
        const currencyId = this.currencyStore.primaryCurrency()!.id!
        this.form.get('currencyId')?.setValue(currencyId)
        this.displayedCurrency.set(this.currencyStore.primaryCurrency()!.code)
      }
    })

    this.form.get('currencyId')?.valueChanges.subscribe((value) => {
      const currency = this.currencyStore.currencies().filter(e => e.id == value)[0]
      this.displayedCurrency.set(currency.code)
    })

  }

  ngOnInit() {
    this.organizationStore.getActiveOrganization()
  }

  onCreateLine (payload: TransactionLine) {
    this.transactionsLines.push(payload)
    this.totalCreditAmount = this.totalCreditAmount + payload.creditAmount
    this.totalDebitAmount = this.totalDebitAmount + payload.debitAmount
    this.balanced = this.totalCreditAmount == this.totalDebitAmount
    this.balance = Math.abs( this.totalCreditAmount - this.totalDebitAmount)
  }

  onDeleteLine (index: number){

  }

  submit(){
    const payload: Transaction = {
      lines: this.transactionsLines,
      date: this.form.get('date')?.value,
      description: this.form.get('description')?.value,
      currencyId: this.form.get('currencyId')?.value
    }

    this.transactionStore.createTransaction(payload)
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
