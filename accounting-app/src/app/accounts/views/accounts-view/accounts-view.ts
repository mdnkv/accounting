import {Component, effect, inject, OnInit, signal} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';

import {CreateAccountModal} from '../../components/create-account-modal/create-account-modal';
import {SortAccountDropdown} from '../../components/sort-account-dropdown/sort-account-dropdown';
import {AccountsTabs} from '../../components/accounts-tabs/accounts-tabs';
import {Account} from '../../models/accounts.models';
import {AccountsList} from '../../components/accounts-list/accounts-list';
import {AccountService} from '../../services/account';

@Component({
  selector: 'app-accounts-view',
  imports: [
    CreateAccountModal,
    SortAccountDropdown,
    AccountsTabs,
    AccountsList
  ],
  templateUrl: './accounts-view.html',
  styleUrl: './accounts-view.css'
})
export class AccountsView implements OnInit{

  accounts: Account[] = []
  shownAccounts: Account[] = []
  accountType = signal<string|null>(null)
  accountService: AccountService = inject(AccountService)

  constructor() {
    effect(() => {
      if (this.accountType() == 'ASSET'){
        this.shownAccounts = this.accounts.filter(acc => acc.accountType == 'ASSET')
      }
      else if (this.accountType() == 'LIABILITY'){
        this.shownAccounts = this.accounts.filter(acc => acc.accountType == 'LIABILITY')
      }
      else if (this.accountType() == 'EQUITY'){
        this.shownAccounts = this.accounts.filter(acc => acc.accountType == 'EQUITY')
      }
      else if (this.accountType() == 'INCOME'){
        this.shownAccounts = this.accounts.filter(acc => acc.accountType == 'INCOME')
      }
      else if (this.accountType() == 'EXPENSE'){
        this.shownAccounts = this.accounts.filter(acc => acc.accountType == 'EXPENSE')
      }
      else {
        this.shownAccounts = this.accounts
      }
    });
  }

  selectAccountType(selection: string | null){
    this.accountType.set(selection)
  }

  onCreateAccount(payload: Account) {
    console.log(payload)
    this.accountService.createAccount(payload).subscribe({
      next: result => {
        this.accounts.push(result)
        if (result.accountType == this.accountType()){
          this.shownAccounts.push(result)
        }
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
      }
    })
  }

  onSortAccounts(order: string){
    if (order == 'name-asc'){
      this.shownAccounts.sort((c1, c2) => {
        return c1.name.localeCompare(c2.name)
      })
    } else {
      this.shownAccounts.sort((c1, c2) => {
        return c2.name.localeCompare(c1.name)
      })
    }
  }

  onDeleteAccount(id: string){
    this.accountService.deleteAccount(id).subscribe({
      next: result => {
        this.accounts = this.accounts.filter(a => a.id! !== id)
        alert('Account was deleted successfully')
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
        if (err.status == 400){
          alert('This account has associated transactions and cannot be deleted. You can make it deprecated instead')
        }
      }
    })
  }

  ngOnInit() {
    this.accountService.getAccounts(true).subscribe({
      next: result => {
        this.accounts = result
        this.shownAccounts = result
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
      }
    })
  }


}
