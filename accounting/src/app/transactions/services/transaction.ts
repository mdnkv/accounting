import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../../environments/environment.development';
import {Transaction} from '../models/transactions.models';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getTransactions(organizationId: string): Observable<Transaction[]> {
    // const organizationId = localStorage.getItem('activeOrganizationId') as string
    return this.http.get<Transaction[]>(`${this.serverUrl}/transactions/organization/${organizationId}`)
  }

  createTransaction(payload: Transaction): Observable<Transaction>{
    return this.http.post<Transaction>(`${this.serverUrl}/transactions/create`, payload)
  }

}
