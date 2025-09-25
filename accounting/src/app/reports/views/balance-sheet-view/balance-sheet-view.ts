import {Component, inject, OnInit, signal} from '@angular/core';
import {CurrencyPipe, DatePipe} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';

import {UserOrganizationStore} from '../../../organizations/stores/user-organization.store';
import {ReportService} from '../../services/report';
import {BalanceSheet} from '../../models/reports.models';

@Component({
  selector: 'app-balance-sheet-view',
  imports: [DatePipe, CurrencyPipe],
  templateUrl: './balance-sheet-view.html',
  styleUrl: './balance-sheet-view.css'
})
export class BalanceSheetView implements OnInit{

  loaded = signal(false)
  data = signal<BalanceSheet|undefined>(undefined)

  readonly userOrganizationStore = inject(UserOrganizationStore)
  reportService = inject(ReportService)

  ngOnInit() {
    if (this.userOrganizationStore.isActiveOrganizationLoaded()){
      const organizationId = this.userOrganizationStore.activeOrganization()!.organization.id!
      this.reportService.getBalanceSheet(organizationId).subscribe({
        next: result => {
          console.log(result)
          this.data.set(result)
          this.loaded.set(true)
        },
        error: (err:HttpErrorResponse) => {
          console.log(err)
          this.loaded.set(false)
        }
      })
    }
  }

}
