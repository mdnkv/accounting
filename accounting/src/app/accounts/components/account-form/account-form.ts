import {Component, effect, inject, input, output, signal} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import {MatCheckboxModule} from '@angular/material/checkbox';

import {Account} from '../../models/accounts.models';

@Component({
  selector: 'app-account-form',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCheckboxModule
  ],
  templateUrl: './account-form.html',
  styleUrl: './account-form.css'
})
export class AccountForm {

  saveAccount = output<Account>()
  currentAccount = input<Account>()

  isUpdate = signal(false)

  formBuilder: FormBuilder = inject(FormBuilder)
  form: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required, Validators.maxLength(255)]],
    code: ['', [Validators.required, Validators.maxLength(25)]],
    accountType: ['ASSET', [Validators.required]],
    deprecated: [false]
  })

  constructor() {
    effect(() => {
      if (this.currentAccount() != undefined){
        this.updateForm(this.currentAccount()!)
        this.isUpdate.set(true)
      } else {
        this.isUpdate.set(false)
      }
    });
  }

  updateForm (payload: Account){
    this.form.get('name')?.setValue(payload.name)
    this.form.get('code')?.setValue(payload.code)
    this.form.get('accountType')?.setValue(payload.accountType)
    this.form.get('deprecated')?.setValue(payload.deprecated)
  }

  onSubmit(){

    if (this.isUpdate()){
      // update
      const payload: Account = {
        ...this.currentAccount()!,
        name: this.form.get('name')?.value,
        code: this.form.get('code')?.value,
        deprecated: this.form.get('deprecated')?.value,
        accountType: this.form.get('accountType')?.value
      }

      this.saveAccount.emit(payload)

    } else {

      const payload: Account = {
        name: this.form.get('name')?.value,
        code: this.form.get('code')?.value,
        accountType: this.form.get('accountType')?.value,
        deprecated: false
      }

      this.saveAccount.emit(payload)

    }

  }

  onCancel() {}

}
