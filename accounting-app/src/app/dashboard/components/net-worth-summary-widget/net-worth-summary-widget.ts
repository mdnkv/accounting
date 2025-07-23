import {Component, effect, inject, OnInit, signal} from '@angular/core';
import {CurrencyPipe} from '@angular/common';
import {WidgetRangeDropdown} from '../widget-range-dropdown/widget-range-dropdown';
import {DashboardStore} from '../../stores/dashboard.store';
import {CurrencyStore} from '../../../currencies/stores/currencies.store';

@Component({
  selector: 'app-net-worth-summary-widget',
  imports: [
    CurrencyPipe,
    WidgetRangeDropdown
  ],
  templateUrl: './net-worth-summary-widget.html',
  styleUrl: './net-worth-summary-widget.css'
})
export class NetWorthSummaryWidget implements OnInit{

  readonly currencyStore = inject(CurrencyStore)
  readonly dashboardStore = inject(DashboardStore)

  currency = signal('EUR')

  constructor() {
    effect(() => {
      if (this.currencyStore.isPrimaryCurrencyLoaded()){
        this.currency.set(this.currencyStore.primaryCurrency()!.code)
      }
    })
  }

  ngOnInit() {
    this.dashboardStore.refreshNetWorthSummary(30)
  }

  refresh(daysCount: number){
    this.dashboardStore.refreshNetWorthSummary(daysCount)
  }

}
