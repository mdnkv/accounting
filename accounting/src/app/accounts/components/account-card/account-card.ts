import {Component, input, output, signal} from '@angular/core';
import {Account} from '../../models/accounts.models';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-account-card',
  imports: [
    MatIconModule,
    MatButtonModule,
    RouterLink
  ],
  templateUrl: './account-card.html',
  styleUrl: './account-card.css'
})
export class AccountCard {

  account= input.required<Account>()
  dropdownActive = signal(false)
  deleteClicked = output<Account>()

  toggleDropdown(){
    this.dropdownActive.update(v => !v)
  }

  delete(){
    this.dropdownActive.set(false)
    this.deleteClicked.emit(this.account())
  }

}
