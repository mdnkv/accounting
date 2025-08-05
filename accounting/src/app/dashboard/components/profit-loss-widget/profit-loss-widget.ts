import {Component, effect, inject, OnInit, signal} from '@angular/core';
import {DashboardWidget} from '../dashboard-widget/dashboard-widget';
import {CurrencyStore} from '../../../currencies/stores/currency.store';
import {DashboardStore} from '../../stores/dashboard.store';
import {WidgetColumn} from '../widget-column/widget-column';

@Component({
  selector: 'app-profit-loss-widget',
  imports: [
    DashboardWidget,
    WidgetColumn
  ],
  templateUrl: './profit-loss-widget.html',
  styleUrl: './profit-loss-widget.css'
})
export class ProfitLossWidget implements OnInit{

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

  onRefresh(daysCount: number){
    this.dashboardStore.refreshProfitLossSummary(daysCount)
  }


}
