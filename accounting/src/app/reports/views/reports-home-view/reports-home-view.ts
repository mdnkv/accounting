import { Component } from '@angular/core';

import {MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';

@Component({
  selector: 'app-reports-home-view',
  imports: [MatCardModule, MatButtonModule],
  templateUrl: './reports-home-view.html',
  styleUrl: './reports-home-view.css'
})
export class ReportsHomeView {

}
