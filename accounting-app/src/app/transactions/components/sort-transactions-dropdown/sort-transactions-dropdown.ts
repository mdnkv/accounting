import {Component, output} from '@angular/core';

@Component({
  selector: 'app-sort-transactions-dropdown',
  imports: [],
  templateUrl: './sort-transactions-dropdown.html',
  styleUrl: './sort-transactions-dropdown.css'
})
export class SortTransactionsDropdown {

  selectOrder = output<string>()

  isActive: boolean = false

  selectSortOrder (order: string){
    this.selectOrder.emit(order)
    this.isActive = false
  }

}
