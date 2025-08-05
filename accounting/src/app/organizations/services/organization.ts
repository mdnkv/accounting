import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';

import {environment} from '../../../environments/environment.development';
import {Organization} from '../models/organizations.models';

@Injectable({
  providedIn: 'root'
})
export class OrganizationService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  createOrganization(payload: Organization): Observable<Organization>{
    return this.http.post<Organization>(`${this.serverUrl}/organizations/create`, payload)
  }

  updateOrganization(payload: Organization): Observable<Organization>{
    return this.http.put<Organization>(`${this.serverUrl}/organizations/update`, payload)
  }

  getOrganization(id: string): Observable<Organization> {
    return this.http.get<Organization>(`${this.serverUrl}/organizations/organization/${id}`)
  }

}
