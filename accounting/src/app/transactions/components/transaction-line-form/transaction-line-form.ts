import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatSelectModule} from '@angular/material/select';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogRef} from '@angular/material/dialog';
import {MatInputModule} from '@angular/material/input';

import {AccountStore} from '../../../accounts/stores/account.store';
import {TransactionLine} from '../../models/transactions.models';

@Component({
  selector: 'app-transaction-line-form',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatIconModule,
    MatSelectModule,
    MatButtonModule,
    MatInputModule,
  ],
  templateUrl: './transaction-line-form.html',
  styleUrl: './transaction-line-form.css'
})
export class TransactionLineForm {

  readonly accountStore = inject(AccountStore)
  formBuilder: FormBuilder = inject(FormBuilder)

  dialogRef:MatDialogRef<TransactionLineForm> = inject(MatDialogRef<TransactionLineForm>)

  form: FormGroup = this.formBuilder.group({
    debitAmount: [0.0, [Validators.required, Validators.min(0)]],
    creditAmount: [0.0, [Validators.required, Validators.min(0)]],
    accountId: [null, [Validators.required]]
  })

  onSubmit(){
    const account = this.accountStore.displayedAccounts().filter(a => a.id! == this.form.get('accountId')?.value)[0]
    const payload: TransactionLine = {
      debitAmount: this.form.get('debitAmount')?.value,
      creditAmount: this.form.get('creditAmount')?.value,
      accountId: this.form.get('accountId')?.value,
      account: account
    }
    this.dialogRef.close(payload)
  }

  onCancel(){
    this.dialogRef.close();
  }

}
