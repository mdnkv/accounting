import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../../environments/environment';
import {ExpenseCategory, NetWorthSummary, ProfitLossSummary} from '../models/reports.models';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getProfitLossSummary(organizationId: string, daysCount : number): Observable<ProfitLossSummary>{
    // const organizationId = localStorage.getItem('activeOrganizationId') as string
    return this.http.get<ProfitLossSummary>(`${this.serverUrl}/dashboard/profit-loss/${organizationId}?daysCount=${daysCount}`)
  }

  getNetWorthSummary(organizationId: string, daysCount: number): Observable<NetWorthSummary>{
    // const organizationId = localStorage.getItem('activeOrganizationId') as string
    return this.http.get<NetWorthSummary>(`${this.serverUrl}/dashboard/net-worth/${organizationId}?daysCount=${daysCount}`)
  }

  getExpenseCategories(organizationId: string, daysCount : number): Observable<ExpenseCategory[]>{
    // const organizationId = localStorage.getItem('activeOrganizationId') as string
    return this.http.get<ExpenseCategory[]>(`${this.serverUrl}/dashboard/expense-categories/${organizationId}?daysCount=${daysCount}`)
  }

}
