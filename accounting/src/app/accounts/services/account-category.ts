import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../../environments/environment';
import {AccountCategory} from '../models/accounts.models';

@Injectable({
  providedIn: 'root'
})
export class AccountCategoryService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getAccountCategories(organizationId: string): Observable<AccountCategory[]>{
    return this.http.get<AccountCategory[]>(`${this.serverUrl}/account-categories/organization/${organizationId}`)
  }

}
