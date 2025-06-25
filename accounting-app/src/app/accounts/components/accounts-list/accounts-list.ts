import {Component, input} from '@angular/core';
import {Account} from '../../models/accounts.models';

@Component({
  selector: 'app-accounts-list',
  imports: [],
  templateUrl: './accounts-list.html',
  styleUrl: './accounts-list.css'
})
export class AccountsList {

  accounts = input.required<Account[]>()

}
