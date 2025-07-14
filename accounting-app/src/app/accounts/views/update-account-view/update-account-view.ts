import {Component, inject, input, OnInit, signal} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import {AccountService} from '../../services/account';
import {Account} from '../../models/accounts.models';

@Component({
  selector: 'app-update-account-view',
  imports: [ReactiveFormsModule],
  templateUrl: './update-account-view.html',
  styleUrl: './update-account-view.css'
})
export class UpdateAccountView implements OnInit{

  id= input.required<string>()

  accountService: AccountService = inject(AccountService)
  formBuilder: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)

  form: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required, Validators.maxLength(255)]],
    code: ['', [Validators.required, Validators.maxLength(20)]],
    deprecated: [false]
  })

  account: Account | undefined = undefined
  loading = signal(true)
  accountType = signal('ASSET')

  submit(){
    this.loading.set(true)
    const payload: Account = {
      ...this.account!,
      name: this.form.get('name')?.value,
      code: this.form.get('code')?.value,
      deprecated: this.form.get('deprecated')?.value,
      accountType: this.accountType()
    }

    this.accountService.updateAccount(payload).subscribe({
      next: result => {
        this.loading.set(false)
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
        this.loading.set(false)
      }
    })
  }

  cancel(){
    if (this.form.touched){
      if (confirm('Do you want to quit without saving?')){
        this.router.navigateByUrl('/accounts')
      }
    } else {
      this.router.navigateByUrl('/accounts')
    }
  }

  loadForm(payload: Account){
    this.account = payload
    this.form.get('name')?.setValue(payload.name)
    this.form.get('code')?.setValue(payload.code)
    this.form.get('deprecated')?.setValue(payload.deprecated)
    this.accountType.set(payload.accountType)
  }

  setAccountType(type: string){
    this.accountType.set(type)
  }

  ngOnInit() {
    this.accountService.getAccount(this.id()).subscribe({
      next: result => {
        this.loadForm(result)
        this.loading.set(false)
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
      }
    })
  }

}
