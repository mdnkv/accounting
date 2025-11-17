import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';

import {environment} from '../../../environments/environment';
import {CreateOrganizationUserRequest, OrganizationUser} from '../models/organizations.models';

@Injectable({
  providedIn: 'root'
})
export class OrganizationUserService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getUsersInOrganization(organizationId: string): Observable<OrganizationUser[]> {
    return this.http.get<OrganizationUser[]>(`${this.serverUrl}/organization-users/organization/${organizationId}`)
  }

  createOrganizationUser(payload: CreateOrganizationUserRequest): Observable<OrganizationUser> {
    return this.http.post<OrganizationUser>(`${this.serverUrl}/organization-users/create`, payload)
  }

  updateOrganizationUser(payload: OrganizationUser): Observable<OrganizationUser>{
    return this.http.put<OrganizationUser>(`${this.serverUrl}/organization-users/update`, payload)
  }

}
