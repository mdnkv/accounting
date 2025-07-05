import {Component, inject, OnInit, signal} from '@angular/core';
import {ReportService} from '../../../reports/services/report';
import {NetWorthSummary} from '../../../reports/models/reports.models';
import {HttpErrorResponse} from '@angular/common/http';
import {CurrencyPipe} from '@angular/common';

@Component({
  selector: 'app-net-worth-summary-widget',
  imports: [
    CurrencyPipe
  ],
  templateUrl: './net-worth-summary-widget.html',
  styleUrl: './net-worth-summary-widget.css'
})
export class NetWorthSummaryWidget implements OnInit{

  reportService: ReportService = inject(ReportService)
  data = signal<NetWorthSummary|undefined>(undefined)
  loading = signal(true)

  ngOnInit() {
    this.reportService.getNetWorthSummary().subscribe({
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
    this.reportService.getNetWorthSummary().subscribe({
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
