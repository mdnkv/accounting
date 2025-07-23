import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../../environments/environment.development';
import {Currency} from '../models/currencies.models';

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getCurrencies(organizationId: string): Observable<Currency[]>{
    return this.http.get<Currency[]>(`${this.serverUrl}/currencies/organization/${organizationId}`)
  }

}
