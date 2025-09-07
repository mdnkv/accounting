import {Component, effect, inject, OnInit, signal} from '@angular/core';
import {Router} from '@angular/router';
import {CurrencyPipe} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

import {MatDialog} from '@angular/material/dialog';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {provideNativeDateAdapter} from '@angular/material/core';

import {CurrencyStore} from '../../../currencies/stores/currency.store';
import {AccountStore} from '../../../accounts/stores/account.store';
import {UserOrganizationStore} from '../../../organizations/stores/user-organization.store';
import {TransactionStore} from '../../stores/transaction.store';
import {Transaction, TransactionLine} from '../../models/transactions.models';
import {TransactionLineCard} from '../../components/transaction-line-card/transaction-line-card';
import {TransactionLineForm} from '../../components/transaction-line-form/transaction-line-form';
import {JournalStore} from '../../../journals/stores/journal.store';

@Component({
  selector: 'app-create-transaction-view',
  imports: [
    CurrencyPipe,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatDatepickerModule,
    TransactionLineCard
  ],
  templateUrl: './create-transaction-view.html',
  styleUrl: './create-transaction-view.css',
  providers: [provideNativeDateAdapter()]
})
export class CreateTransactionView implements OnInit{

  readonly accountStore = inject(AccountStore)
  readonly userOrganizationStore = inject(UserOrganizationStore)
  readonly currencyStore = inject(CurrencyStore)
  readonly transactionStore = inject(TransactionStore)
  readonly journalStore = inject(JournalStore)

  formBuilder: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)
  dialog: MatDialog = inject(MatDialog)

  form: FormGroup = this.formBuilder.group({
    date: [null, [Validators.required]],
    description: ['', [Validators.required]],
    currencyId: [null, [Validators.required]],
    journalId: [null, [Validators.required]]
  })

  transactionsLines = signal<TransactionLine[]>([])
  totalCreditAmount = signal(0.0)
  totalDebitAmount =  signal(0.0)
  balance = signal(0.0)
  balanced = signal(true)
  displayedCurrency = signal<string>('EUR')

  constructor() {
    effect(() => {
      if (this.userOrganizationStore.isActiveOrganizationLoaded()){
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

    if (this.journalStore.areJournalsLoaded()){
      const defaultJournalId = this.journalStore.journals()[0].id!
      this.form.get('journalId')?.setValue(defaultJournalId)
    }

    this.form.get('currencyId')?.valueChanges.subscribe((value) => {
      const currency = this.currencyStore.currencies().filter(e => e.id == value)[0]
      this.displayedCurrency.set(currency.code)
    })

  }

  ngOnInit() {
    this.userOrganizationStore.loadActiveOrganization()
    this.journalStore.getJournals()
  }

  onCreateLine (payload: TransactionLine) {
    this.transactionsLines().push(payload)
    this.totalCreditAmount.update(value => value + payload.creditAmount)
    this.totalDebitAmount.update(value => value + payload.debitAmount)
    this.balanced.set(this.totalDebitAmount() == this.totalCreditAmount())
    this.balance.set( Math.abs( this.totalCreditAmount() - this.totalDebitAmount()))
  }

  onOpenTransactionLineForm(){
    let dialogRef = this.dialog.open(TransactionLineForm, {
      width: '600px'
    })
    dialogRef.afterClosed().subscribe(result => {
      if (result){
        console.log(result)
        this.onCreateLine(result)
      }
    })
  }

  onSubmit(){
    const payload: Transaction = {
      lines: this.transactionsLines(),
      date: this.form.get('date')?.value,
      description: this.form.get('description')?.value,
      currencyId: this.form.get('currencyId')?.value,
      journalId: this.form.get('journalId')?.value
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
