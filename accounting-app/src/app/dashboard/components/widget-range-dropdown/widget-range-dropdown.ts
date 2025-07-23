import {Component, input, output, signal} from '@angular/core';

@Component({
  selector: 'app-widget-range-dropdown',
  imports: [],
  templateUrl: './widget-range-dropdown.html',
  styleUrl: './widget-range-dropdown.css'
})
export class WidgetRangeDropdown {

  active = signal(false)
  // loading = input.required<boolean>()
  selectRange = output<number>()

  toggleDropdown(){
    this.active.update(value => !value)
  }

  onRangeSelected(daysCount: number){
    this.selectRange.emit(daysCount)
    this.active.set(false)
  }

}
