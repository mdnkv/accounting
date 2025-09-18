import {Component, output, signal} from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-widget-refresh-menu',
  imports: [MatIconModule, MatButtonModule],
  templateUrl: './widget-refresh-menu.html',
  styleUrl: './widget-refresh-menu.css'
})
export class WidgetRefreshMenu {

  active = signal(false)
  selectDuration = output<number>()

  toggleMenu(){
    this.active.update(v => !v)
  }

  onSelectItem(value: number){
    this.selectDuration.emit(value)
    this.active.set(false)
  }

}
