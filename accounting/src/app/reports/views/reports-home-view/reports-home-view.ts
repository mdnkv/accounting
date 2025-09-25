import {Component, inject} from '@angular/core';

import {MatButtonModule} from '@angular/material/button';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-reports-home-view',
  imports: [MatButtonModule, RouterLink],
  templateUrl: './reports-home-view.html',
  styleUrl: './reports-home-view.css'
})
export class ReportsHomeView {


}
