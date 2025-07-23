import {Component, inject, output} from '@angular/core';
import {AccountStore} from '../../stores/accounts.store';

@Component({
  selector: 'app-sort-account-dropdown',
  imports: [],
  templateUrl: './sort-account-dropdown.html',
  styleUrl: './sort-account-dropdown.css'
})
export class SortAccountDropdown {

  readonly accountStore = inject(AccountStore)
  isActive: boolean = false

  selectSortOrder (order: string){
    this.isActive = false
    this.accountStore.setSortOrder(order)
  }

}
