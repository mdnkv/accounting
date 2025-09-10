import {Component, inject} from '@angular/core';
import {Account} from '../../models/accounts.models';
import {AccountForm} from '../../components/account-form/account-form';
import {AccountStore} from '../../stores/account.store';
import {Router} from '@angular/router';

@Component({
  selector: 'app-create-account-view',
  imports: [
    AccountForm
  ],
  templateUrl: './create-account-view.html',
  styleUrl: './create-account-view.css'
})
export class CreateAccountView {

  router: Router = inject(Router)
  readonly accountStore = inject(AccountStore)

  onCreateAccount(account: Account) {
    this.accountStore.createAccount(account)
  }

  onCancel(){
    this.router.navigateByUrl('/accounts')
  }

}
