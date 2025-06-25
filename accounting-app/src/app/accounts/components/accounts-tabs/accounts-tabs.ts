import {Component, output, signal} from '@angular/core';

@Component({
  selector: 'app-accounts-tabs',
  imports: [],
  templateUrl: './accounts-tabs.html',
  styleUrl: './accounts-tabs.css'
})
export class AccountsTabs {

  selected = signal<string|null>(null)

  selectType = output<string|null>()

  onSelectType (selection : string | null){
    this.selected.set(selection)
    this.selectType.emit(selection)
  }

}
