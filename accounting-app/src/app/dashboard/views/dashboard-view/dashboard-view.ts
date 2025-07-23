import {Component, effect, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {ProfitLossSummaryWidget} from '../../components/profit-loss-summary-widget/profit-loss-summary-widget';
import {NetWorthSummaryWidget} from '../../components/net-worth-summary-widget/net-worth-summary-widget';
import {ExpenseCategoriesWidget} from '../../components/expense-categories-widget/expense-categories-widget';
import {OrganizationStore} from '../../../organizations/stores/organizations.store';
import {CurrencyStore} from '../../../currencies/stores/currencies.store';

@Component({
  selector: 'app-dashboard-view',
  imports: [
    ProfitLossSummaryWidget,
    NetWorthSummaryWidget,
    ExpenseCategoriesWidget
  ],
  templateUrl: './dashboard-view.html',
  styleUrl: './dashboard-view.css'
})
export class DashboardView implements OnInit{

  readonly organizationStore = inject(OrganizationStore)
  readonly currencyStore = inject(CurrencyStore)
  router: Router = inject(Router)

  constructor() {
    effect(() => {
      if (this.organizationStore.isActiveOrganizationLoaded()){
        if (this.organizationStore.activeOrganization() == undefined) {
          this.router.navigateByUrl('/organizations/create')
        }
        console.log(this.organizationStore.activeOrganization())
      }
    })
  }

  ngOnInit() {
    this.organizationStore.getActiveOrganization()
    this.currencyStore.getCurrencies()
  }
}
