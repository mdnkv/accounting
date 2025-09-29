import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../../environments/environment';
import {BalanceSheet, ExpenseCategory, NetWorthSummary, ProfitLossSummary} from '../models/reports.models';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getProfitLossSummary(organizationId: string, daysCount : number): Observable<ProfitLossSummary>{
    return this.http.get<ProfitLossSummary>(`${this.serverUrl}/dashboard/profit-loss/${organizationId}?daysCount=${daysCount}`)
  }

  getNetWorthSummary(organizationId: string, daysCount: number): Observable<NetWorthSummary>{
    return this.http.get<NetWorthSummary>(`${this.serverUrl}/dashboard/net-worth/${organizationId}?daysCount=${daysCount}`)
  }

  getExpenseCategories(organizationId: string, daysCount : number): Observable<ExpenseCategory[]>{
    return this.http.get<ExpenseCategory[]>(`${this.serverUrl}/dashboard/expense-categories/${organizationId}?daysCount=${daysCount}`)
  }

  getBalanceSheet(organizationId: string): Observable<BalanceSheet>{
    return this.http.get<BalanceSheet>(`${this.serverUrl}/reports/balance-sheet/${organizationId}`)
  }

}
