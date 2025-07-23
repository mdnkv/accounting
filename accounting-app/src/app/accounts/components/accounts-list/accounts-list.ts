import {Component, inject, input, output} from '@angular/core';
import {RouterLink} from '@angular/router';
import {AccountStore} from '../../stores/accounts.store';

@Component({
  selector: 'app-accounts-list',
  imports: [RouterLink],
  templateUrl: './accounts-list.html',
  styleUrl: './accounts-list.css'
})
export class AccountsList {

  readonly accountStore = inject(AccountStore)

  onDelete(id: string){
    // if (confirm('Do you want to remove this account? This cannot be undone')){
    //   this.deleteAccount.emit(id)
    // }
  }

}
