import {Component, inject, output, signal} from '@angular/core';
import {AccountStore} from '../../stores/accounts.store';

@Component({
  selector: 'app-accounts-tabs',
  imports: [],
  templateUrl: './accounts-tabs.html',
  styleUrl: './accounts-tabs.css'
})
export class AccountsTabs {
  readonly accountStore = inject(AccountStore)
}
