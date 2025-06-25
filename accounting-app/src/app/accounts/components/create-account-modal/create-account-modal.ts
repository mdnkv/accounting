import {Component, inject, output, signal} from '@angular/core';
import {Account} from '../../models/accounts.models';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-create-account-modal',
  imports: [ReactiveFormsModule],
  templateUrl: './create-account-modal.html',
  styleUrl: './create-account-modal.css'
})
export class CreateAccountModal {

  createAccount = output<Account>()
  showModal = signal<boolean>(false)
  accountType = signal<string>('ASSET')

  formBuilder: FormBuilder = inject(FormBuilder)
  form: FormGroup = this.formBuilder.group({
    code: ['', [Validators.required, Validators.maxLength(20)]],
    name: ['', [Validators.required, Validators.maxLength(255)]],
  })

  submit(){
    const organizationId = localStorage.getItem('activeOrganizationId') as string
    const payload: Account = {
      name: this.form.get('name')?.value,
      code: this.form.get('code')?.value,
      accountType: this.accountType(),
      organizationId
    }
    this.createAccount.emit(payload)
    this.showModal.set(false)
    this.setAccountType('ASSET')
    this.form.reset()
  }

  closeModal(){
    if (this.form.touched) {
       if (confirm('Do you want to close this window and lose all data?')){
         this.showModal.set(false)
         this.setAccountType('ASSET')
         this.form.reset()
       }
    } else {
      this.showModal.set(false)
      this.setAccountType('ASSET')
    }
  }

  openModal(){
    this.showModal.set(true)
  }

  setAccountType(type: string){
    this.accountType.set(type)
  }

}
