import {Component, inject, output, signal} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

import {TransactionLine} from '../../models/transactions.models';
import {AccountStore} from '../../../accounts/stores/accounts.store';

@Component({
  selector: 'app-create-transaction-line-modal',
  imports: [ReactiveFormsModule],
  templateUrl: './create-transaction-line-modal.html',
  styleUrl: './create-transaction-line-modal.css'
})
export class CreateTransactionLineModal{

  readonly accountStore = inject(AccountStore)
  createTransactionLine = output<TransactionLine>()
  error = signal<boolean>(false)
  isActive = signal<boolean>(false)

  formBuilder: FormBuilder = inject(FormBuilder)

  form: FormGroup = this.formBuilder.group({
    accountId: [null, [Validators.required]],
    debitAmount: [0.0, [Validators.required, Validators.min(0.0)]],
    creditAmount: [0.0, [Validators.required, Validators.min(0.0)]]
  })

  submit(){
    const account = this.accountStore.displayedAccounts().filter(a => a.id! == this.form.get('accountId')?.value)[0]
    const payload: TransactionLine = {
      debitAmount: this.form.get('debitAmount')?.value,
      creditAmount: this.form.get('creditAmount')?.value,
      accountId: this.form.get('accountId')?.value,
      account: account
    }

    this.createTransactionLine.emit(payload)
    this.isActive.set(false)
    this.form.reset()
    this.form.get('creditAmount')?.setValue(0.0)
    this.form.get('debitAmount')?.setValue(0.0)
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
