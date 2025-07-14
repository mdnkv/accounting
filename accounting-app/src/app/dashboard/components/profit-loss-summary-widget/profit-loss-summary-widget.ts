import {Component, inject, OnInit, signal} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {CurrencyPipe} from '@angular/common';

import {ReportService} from '../../../reports/services/report';
import {ProfitLossSummary} from '../../../reports/models/reports.models';
import {WidgetRangeDropdown} from '../widget-range-dropdown/widget-range-dropdown';

@Component({
  selector: 'app-profit-loss-summary-widget',
  imports: [CurrencyPipe, WidgetRangeDropdown],
  templateUrl: './profit-loss-summary-widget.html',
  styleUrl: './profit-loss-summary-widget.css'
})
export class ProfitLossSummaryWidget implements OnInit{

  reportService: ReportService = inject(ReportService)
  data = signal<ProfitLossSummary|undefined>(undefined)
  loading = signal(true)
  days = signal(30)

  ngOnInit() {
    this.refresh(this.days())
  }

  refresh(daysCount: number){
    this.loading.set(true)
    this.data.set(undefined)
    this.days.set(daysCount)
    this.reportService.getProfitLossSummary(daysCount).subscribe({
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
