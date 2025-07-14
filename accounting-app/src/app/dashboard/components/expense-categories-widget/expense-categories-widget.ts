import {Component, inject, OnInit, signal} from '@angular/core';
import {CurrencyPipe} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';

import {ExpenseCategory} from '../../../reports/models/reports.models';
import {ReportService} from '../../../reports/services/report';
import {WidgetRangeDropdown} from '../widget-range-dropdown/widget-range-dropdown';

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

  data: ExpenseCategory[] = []
  loading = signal(true)
  days = signal(30)

  reportService: ReportService = inject(ReportService)

  ngOnInit() {
    this.refresh(this.days())
  }

  refresh(daysCount: number){
    this.loading.set(true)
    this.days.set(daysCount)
    this.reportService.getExpenseCategories(daysCount).subscribe({
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
