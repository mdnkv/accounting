import {Component, inject, OnInit, signal} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {CurrencyPipe} from '@angular/common';

import {ReportService} from '../../../reports/services/report';
import {ProfitLossSummary} from '../../../reports/models/reports.models';

@Component({
  selector: 'app-profit-loss-summary-widget',
  imports: [CurrencyPipe],
  templateUrl: './profit-loss-summary-widget.html',
  styleUrl: './profit-loss-summary-widget.css'
})
export class ProfitLossSummaryWidget implements OnInit{

  reportService: ReportService = inject(ReportService)
  data = signal<ProfitLossSummary|undefined>(undefined)
  loading = signal(true)

  ngOnInit() {
    this.reportService.getProfitLossSummary().subscribe({
      next: result => {
        this.data.set(result)
        this.loading.set(false)
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
      }
    })
  }

  refresh(){
    this.loading.set(true)
    this.data.set(undefined)
    this.reportService.getProfitLossSummary().subscribe({
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
