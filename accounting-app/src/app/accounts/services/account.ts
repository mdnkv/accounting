import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment.development';
import {Observable} from 'rxjs';
import {Account} from '../models/accounts.models';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getAccounts(): Observable<Account[]>{
    const organizationId = localStorage.getItem('activeOrganizationId') as string
    return this.http.get<Account[]>(`${this.serverUrl}/accounts/organization/${organizationId}`)
  }

  createAccount(payload: Account):  Observable<Account>{
    return this.http.post<Account>(`${this.serverUrl}/accounts/create`, payload)
  }

}
