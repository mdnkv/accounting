import {Component, effect, inject, OnInit, signal} from '@angular/core';
import {DashboardWidget} from "../dashboard-widget/dashboard-widget";
import {CurrencyStore} from '../../../currencies/stores/currency.store';
import {DashboardStore} from '../../stores/dashboard.store';

@Component({
  selector: 'app-expense-categories-widget',
    imports: [DashboardWidget],
  templateUrl: './expense-categories-widget.html',
  styleUrl: './expense-categories-widget.css'
})
export class ExpenseCategoriesWidget implements OnInit{

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
    this.dashboardStore.refreshExpenseCategories(30)
  }

  onRefresh(daysCount: number){
    this.dashboardStore.refreshExpenseCategories(daysCount)
  }

}
