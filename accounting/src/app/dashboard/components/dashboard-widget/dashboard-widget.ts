import {Component, output} from '@angular/core';

import {MatCardModule} from '@angular/material/card';
import {WidgetRefreshMenu} from '../widget-refresh-menu/widget-refresh-menu';

@Component({
  selector: 'app-dashboard-widget',
  imports: [MatCardModule, WidgetRefreshMenu],
  templateUrl: './dashboard-widget.html',
  styleUrl: './dashboard-widget.css'
})
export class DashboardWidget {

  refresh = output<number>()

  onRefresh(daysCount: number){
    this.refresh.emit(daysCount)
  }

}
