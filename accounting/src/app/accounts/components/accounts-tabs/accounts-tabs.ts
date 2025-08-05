import {Component, inject} from '@angular/core';

import {MatButtonToggleModule} from '@angular/material/button-toggle';

import {AccountStore} from '../../stores/account.store';

@Component({
  selector: 'app-accounts-tabs',
  imports: [MatButtonToggleModule],
  templateUrl: './accounts-tabs.html',
  styleUrl: './accounts-tabs.css'
})
export class AccountsTabs {

  readonly store = inject(AccountStore)


}
