import {Component, effect, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {UserOrganizationStore} from '../../../organizations/stores/user-organization.store';
import {ProfitLossWidget} from '../../components/profit-loss-widget/profit-loss-widget';
import {NetWorthWidget} from '../../components/net-worth-widget/net-worth-widget';
import {ExpenseCategoriesWidget} from '../../components/expense-categories-widget/expense-categories-widget';
import {LoadingPlaceholder} from '../../../core/components/loading-placeholder/loading-placeholder';
import {UserStore} from '../../../users/stores/user.store';

@Component({
  selector: 'app-dashboard-view',
  imports: [ProfitLossWidget, NetWorthWidget, ExpenseCategoriesWidget, LoadingPlaceholder],
  templateUrl: './dashboard-view.html',
  styleUrl: './dashboard-view.css'
})
export class DashboardView implements OnInit {

  readonly userStore = inject(UserStore)
  readonly userOrganizationStore = inject(UserOrganizationStore)
  router: Router = inject(Router)

  constructor() {
    effect(() => {

      if (this.userOrganizationStore.isActiveOrganizationLoaded()){
        if (this.userOrganizationStore.activeOrganization() == undefined) {
          // go to create organization view
          this.router.navigateByUrl('/organizations/create')
        } else {
          // load dashboard store
        }
      }

    })
  }

  ngOnInit() {
    this.userStore.loadCurrentUser()
    this.userOrganizationStore.loadActiveOrganization()
  }

}
