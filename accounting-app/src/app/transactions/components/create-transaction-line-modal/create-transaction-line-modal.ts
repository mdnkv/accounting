import {Component, inject, OnInit, output, signal} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

import {TransactionLine} from '../../models/transactions.models';
import {AccountService} from '../../../accounts/services/account';
import {Account} from '../../../accounts/models/accounts.models';

@Component({
  selector: 'app-create-transaction-line-modal',
  imports: [ReactiveFormsModule],
  templateUrl: './create-transaction-line-modal.html',
  styleUrl: './create-transaction-line-modal.css'
})
export class CreateTransactionLineModal implements OnInit{

  createTransactionLine = output<TransactionLine>()
  error = signal<boolean>(false)
  isActive = signal<boolean>(false)

  formBuilder: FormBuilder = inject(FormBuilder)
  accountService: AccountService = inject(AccountService)

  form: FormGroup = this.formBuilder.group({
    accountId: [null, [Validators.required]],
    debitAmount: [0.0, [Validators.required, Validators.min(0.0)]],
    creditAmount: [0.0, [Validators.required, Validators.min(0.0)]]
  })

  accounts: Account[] = []

  ngOnInit() {
    this.accountService.getAccounts().subscribe({
      next: result => {
        this.accounts = result
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

  submit(){
    const account = this.accounts.filter(a => a.id! == this.form.get('accountId')?.value)[0]
    const payload: TransactionLine = {
      debitAmount: this.form.get('debitAmount')?.value,
      creditAmount: this.form.get('creditAmount')?.value,
      accountId: this.form.get('accountId')?.value,
      account: account
    }

    this.createTransactionLine.emit(payload)
    this.isActive.set(false)
    this.form.reset()
  }

  openModal(){
    this.isActive.set(true)
  }

  closeModal() {
    if (this.form.touched) {
      if (confirm('Do you want to cancel?')){
        this.form.reset()
        this.isActive.set(false)
      }
    } else {
      this.isActive.set(false)
    }
  }

}
