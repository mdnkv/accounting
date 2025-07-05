import {Component, inject, OnInit, signal} from '@angular/core';
import {CurrencyPipe} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';

import {ExpenseCategory} from '../../../reports/models/reports.models';
import {ReportService} from '../../../reports/services/report';

@Component({
  selector: 'app-expense-categories-widget',
  imports: [
    CurrencyPipe
  ],
  templateUrl: './expense-categories-widget.html',
  styleUrl: './expense-categories-widget.css'
})
export class ExpenseCategoriesWidget implements OnInit{

  data: ExpenseCategory[] = []
  loading = signal(true)

  reportService: ReportService = inject(ReportService)

  ngOnInit() {
    this.reportService.getExpenseCategories().subscribe({
      next: result => {
        this.loading.set(false)
        this.data = result
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
      }
    })
  }

  refresh(){
    this.loading.set(true)
    this.reportService.getExpenseCategories().subscribe({
      next: result => {
        this.loading.set(false)
        this.data = result
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
      }
    })
  }

}
