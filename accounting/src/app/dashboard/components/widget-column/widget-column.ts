import {Component, input} from '@angular/core';
import {CurrencyPipe} from '@angular/common';

@Component({
  selector: 'app-widget-column',
  imports: [CurrencyPipe],
  templateUrl: './widget-column.html',
  styleUrl: './widget-column.css'
})
export class WidgetColumn {

  currency = input.required<string>()
  amount = input.required<number>()

}
