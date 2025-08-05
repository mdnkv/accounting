import {Component, output} from '@angular/core';

import {MatCardModule} from '@angular/material/card';

@Component({
  selector: 'app-dashboard-widget',
  imports: [MatCardModule],
  templateUrl: './dashboard-widget.html',
  styleUrl: './dashboard-widget.css'
})
export class DashboardWidget {

  refresh = output<number>()

  onRefresh(daysCount: number){
    this.refresh.emit(daysCount)
  }

}
