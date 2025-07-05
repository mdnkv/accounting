import {Component, input, output} from '@angular/core';
import {Account} from '../../models/accounts.models';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-accounts-list',
  imports: [RouterLink],
  templateUrl: './accounts-list.html',
  styleUrl: './accounts-list.css'
})
export class AccountsList {

  accounts = input.required<Account[]>()
  deleteAccount = output<string>()

  onDelete(id: string){
    if (confirm('Do you want to remove this account? This cannot be undone')){
      this.deleteAccount.emit(id)
    }
  }

}
