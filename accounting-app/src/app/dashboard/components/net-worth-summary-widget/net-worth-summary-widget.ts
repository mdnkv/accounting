import {Component, inject, OnInit, signal} from '@angular/core';
import {ReportService} from '../../../reports/services/report';
import {NetWorthSummary} from '../../../reports/models/reports.models';
import {HttpErrorResponse} from '@angular/common/http';
import {CurrencyPipe} from '@angular/common';
import {WidgetRangeDropdown} from '../widget-range-dropdown/widget-range-dropdown';

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

  reportService: ReportService = inject(ReportService)
  data = signal<NetWorthSummary|undefined>(undefined)
  loading = signal(true)
  days = signal(30)

  ngOnInit() {
    this.refresh(this.days())
  }

  refresh(daysCount: number){
    this.loading.set(true)
    this.data.set(undefined)
    this.days.set(daysCount)
    this.reportService.getNetWorthSummary(daysCount).subscribe({
      next: result => {
        this.data.set(result)
        this.loading.set(false)
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
      }
    })
  }

}
