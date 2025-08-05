import {Component, effect, inject, OnInit, signal} from '@angular/core';
import {DashboardWidget} from "../dashboard-widget/dashboard-widget";
import {WidgetColumn} from "../widget-column/widget-column";
import {DashboardStore} from '../../stores/dashboard.store';
import {CurrencyStore} from '../../../currencies/stores/currency.store';

@Component({
  selector: 'app-net-worth-widget',
    imports: [
        DashboardWidget,
        WidgetColumn
    ],
  templateUrl: './net-worth-widget.html',
  styleUrl: './net-worth-widget.css'
})
export class NetWorthWidget implements OnInit{

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

  onRefresh(daysCount: number){
    this.dashboardStore.refreshNetWorthSummary(daysCount)
  }

}
