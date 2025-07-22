import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {map, Observable} from 'rxjs';

import {environment} from '../../../environments/environment.development';
import {UserOrganization} from '../models/organizations.models';

@Injectable({
  providedIn: 'root'
})
export class OrganizationUserService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getActiveForUser (): Observable<UserOrganization> {
    return this.http.get<UserOrganization>(`${this.serverUrl}/organization-users/current/active`).pipe(
      map(result => {
        localStorage.setItem('activeOrganizationId', result.organization.id!)
        return result
      })
    )
  }

  setActiveForUser (id: string): Observable<UserOrganization> {
    return this.http.post<UserOrganization>(`${this.serverUrl}/organization-users/current/active/${id}`, {}).pipe(
      map(result => {
        localStorage.setItem('activeOrganizationId', result.organization.id!)
        return result
      })
    )
  }

  getAllForUser(): Observable<UserOrganization[]> {
    return this.http.get<UserOrganization[]>(`${this.serverUrl}/organization-users/current/all`)
  }

}
