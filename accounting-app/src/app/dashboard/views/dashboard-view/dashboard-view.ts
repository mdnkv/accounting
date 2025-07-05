import {Component, inject, OnInit, signal} from '@angular/core';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

import {RoleService} from '../../../roles/services/role';
import {ProfitLossSummaryWidget} from '../../components/profit-loss-summary-widget/profit-loss-summary-widget';
import {NetWorthSummaryWidget} from '../../components/net-worth-summary-widget/net-worth-summary-widget';
import {ExpenseCategoriesWidget} from '../../components/expense-categories-widget/expense-categories-widget';

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

  loading = signal(true)
  router: Router = inject(Router)
  roleService: RoleService = inject(RoleService)

  ngOnInit() {
    this.roleService.getActiveRole().subscribe({
      next: result => {
        console.log(result)
        this.loading.set(false)
      },
      error: (err: HttpErrorResponse)=> {
        console.log(err)
        if (err.status == 404){
          // redirect to roles list view
          this.router.navigateByUrl('/organizations/create')
        }
      }
    })
  }

}
