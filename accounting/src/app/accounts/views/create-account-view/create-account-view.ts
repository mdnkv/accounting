import {Component, inject} from '@angular/core';
import {Account} from '../../models/accounts.models';
import {AccountForm} from '../../components/account-form/account-form';
import {AccountStore} from '../../stores/account.store';

@Component({
  selector: 'app-create-account-view',
  imports: [
    AccountForm
  ],
  templateUrl: './create-account-view.html',
  styleUrl: './create-account-view.css'
})
export class CreateAccountView {

  readonly accountStore = inject(AccountStore)

  onCreateAccount(account: Account) {
    this.accountStore.createAccount(account)
  }

}
