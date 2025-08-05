import { Component } from '@angular/core';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';

@Component({
  selector: 'app-loading-placeholder',
  imports: [MatProgressSpinnerModule],
  templateUrl: './loading-placeholder.html',
  styleUrl: './loading-placeholder.css'
})
export class LoadingPlaceholder {

}
