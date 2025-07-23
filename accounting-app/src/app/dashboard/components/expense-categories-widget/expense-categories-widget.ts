import {Component, effect, inject, OnInit, signal} from '@angular/core';
import {CurrencyPipe} from '@angular/common';

import {WidgetRangeDropdown} from '../widget-range-dropdown/widget-range-dropdown';
import {DashboardStore} from '../../stores/dashboard.store';
import {CurrencyStore} from '../../../currencies/stores/currencies.store';

@Component({
  selector: 'app-expense-categories-widget',
  imports: [
    CurrencyPipe,
    WidgetRangeDropdown
  ],
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

  refresh(daysCount: number){
    this.dashboardStore.refreshExpenseCategories(daysCount)
  }

}
