import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {map, Observable} from 'rxjs';

import {environment} from '../../../environments/environment.development';
import {UserOrganization} from '../models/organizations.models';

@Injectable({
  providedIn: 'root'
})
export class UserOrganizationService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getActiveOrganizationForUser (): Observable<UserOrganization> {
    return this.http.get<UserOrganization>(`${this.serverUrl}/organization-users/current/active`)
  }

  setActiveOrganizationForUser (id: string): Observable<UserOrganization> {
    return this.http.post<UserOrganization>(`${this.serverUrl}/organization-users/current/active/${id}`, {})
  }

  getOrganizationsForUser(): Observable<UserOrganization[]> {
    return this.http.get<UserOrganization[]>(`${this.serverUrl}/organization-users/current/all`)
  }

}
