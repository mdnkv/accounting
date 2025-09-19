import {Component, input, output, signal} from '@angular/core';
import {RouterLink} from '@angular/router';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {Journal} from '../../models/journals.models';

@Component({
  selector: 'app-journal-card',
  imports: [RouterLink, MatIconModule, MatButtonModule],
  templateUrl: './journal-card.html',
  styleUrl: './journal-card.css'
})
export class JournalCard {

  journal = input.required<Journal>()
  deleteClicked = output<Journal>()

  dropdownActive = signal(false)

  toggleDropdown(){
    this.dropdownActive.update(v => !v)
  }

  delete(){
    this.dropdownActive.set(false)
    this.deleteClicked.emit(this.journal())

  }

}
