import {Component, effect, inject, OnInit, signal} from '@angular/core';
import {CurrencyPipe} from '@angular/common';

import {WidgetRangeDropdown} from '../widget-range-dropdown/widget-range-dropdown';
import {DashboardStore} from '../../stores/dashboard.store';
import {CurrencyStore} from '../../../currencies/stores/currencies.store';

@Component({
  selector: 'app-profit-loss-summary-widget',
  imports: [CurrencyPipe, WidgetRangeDropdown],
  templateUrl: './profit-loss-summary-widget.html',
  styleUrl: './profit-loss-summary-widget.css'
})
export class ProfitLossSummaryWidget implements OnInit{

  readonly dashboardStore = inject(DashboardStore)
  readonly currencyStore = inject(CurrencyStore)

  currency = signal('EUR')

  ngOnInit() {
    this.dashboardStore.refreshProfitLossSummary(30)
  }

  constructor() {
    effect(() => {
      if (this.currencyStore.isPrimaryCurrencyLoaded()){
        this.currency.set(this.currencyStore.primaryCurrency()!.code)
      }
    })
  }

  refresh(daysCount: number){
    this.dashboardStore.refreshProfitLossSummary(daysCount)
  }

}
