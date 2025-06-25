import {Component, output} from '@angular/core';

@Component({
  selector: 'app-sort-account-dropdown',
  imports: [],
  templateUrl: './sort-account-dropdown.html',
  styleUrl: './sort-account-dropdown.css'
})
export class SortAccountDropdown {

  selectOrder = output<string>()

  isActive: boolean = false

  selectSortOrder (order: string){
    this.selectOrder.emit(order)
    this.isActive = false
  }

}
