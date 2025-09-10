import {Component, inject, input, OnInit, signal} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {Account} from '../../models/accounts.models';
import {AccountForm} from '../../components/account-form/account-form';
import {AccountStore} from '../../stores/account.store';
import {AccountService} from '../../services/account';
import {Router} from '@angular/router';

@Component({
  selector: 'app-update-account-view',
  imports: [
    AccountForm
  ],
  templateUrl: './update-account-view.html',
  styleUrl: './update-account-view.css'
})
export class UpdateAccountView implements OnInit {

  readonly accountStore = inject(AccountStore)
  accountService: AccountService = inject(AccountService)
  router: Router = inject(Router)

  id = input.required<string>()
  currentAccount = signal<Account|undefined>(undefined)
  isLoading = signal(true)
  isSuccess = signal(false)
  error = signal('')

  ngOnInit() {
    this.accountService.getAccount(this.id()).subscribe({
      next: result => {
        this.currentAccount.set(result)
      },
      error: (err: HttpErrorResponse)=> {
        console.log(err)
      }
    })
  }

  onUpdateAccount(account: Account) {
    this.accountStore.updateAccount(account)
  }

  onCancel(){
    this.router.navigateByUrl('/accounts')
  }

}
